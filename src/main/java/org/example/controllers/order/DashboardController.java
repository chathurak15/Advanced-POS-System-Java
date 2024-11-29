package org.example.controllers.order;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.dto.OrderDTO;
import org.example.dto.OrderItemDTO;
import org.example.dto.ProductDTO;
import org.example.entity.OrderItem;
import org.example.service.AIService;
import org.example.service.custom.OfferService;
import org.example.service.custom.OrderItemService;
import org.example.service.custom.OrderService;
import org.example.service.custom.impl.ProductServiceIMPL;
import org.example.tm.OrderItemTM;
import org.example.tm.ProductTM;
import org.example.tm.SupplierTM;

import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DashboardController {

    public Text txtDate;
    public Text txtName;
    public ComboBox cmbProduct;
    public TextField txtQuantity;
    public TextField txtNumber;
    public TableView<OrderItemTM> tblProducts;
    public TableColumn<OrderItemTM,Integer>  colId;
    public TableColumn<OrderItemTM,String>  colName;
    public TableColumn<OrderItemTM,Integer>  colQty;
    public TableColumn<OrderItemTM, Double>  colPrice;
    public TableColumn<OrderItemTM, Double>  colSellPrice;
    public TableColumn<OrderItemTM, Double>  colTotal;
    public TableColumn<OrderItemTM, Button>  colAction;
    public Label txtProducts;
    public Label txtTotalPrice;
    public Label txtDiscount;
    public Label txtSubtotal;
    public ComboBox cmbPaymentMethod;

    public int quantity;
    public double totalPrice=0;
    public double DiscountPrice=0;
    public int productcount = 0;
    public double subtotal=0;

    private static final int MAX_DISCOUNT = 100;
    private static final int MIN_DISCOUNT = 0;

    private final ProductServiceIMPL productService = new ProductServiceIMPL();
    private final AIService aiService = new AIService();
    private final OrderService orderService = new OrderService();
    public AnchorPane mainPane;
    private ObservableList<OrderItemTM> observableOrderItems = FXCollections.observableArrayList();
    private final OfferService offerService = new OfferService();
    private OrderItemTM orderItemTM = new OrderItemTM();
    private List<String> orderItems = FXCollections.observableArrayList();

    public void initialize() {
        cmbPaymentMethod.getItems().addAll("Cash", "Card");
        updateDateTime();
        getSelectedProductId();
        visulizeTable();
    }

    private boolean isProductAlreadyAdded(int productId) {
        return observableOrderItems.stream().anyMatch(item -> item.getOrderItemId() == productId);
    }

    public void addOnClick(ActionEvent actionEvent) {
        String quantityText = txtQuantity.getText();
        int productId = getSelectedProductId();
        ProductDTO productDTO = productService.search(productId);
        quantity = Integer.parseInt((quantityText));

        if (getSelectedProductId() <= 0) {
            showAlert(Alert.AlertType.ERROR, "Product Select", "Please Select Product First!");
            return;
        }

        if (Integer.parseInt(quantityText)==0|| !quantityText.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please enter a valid numeric quantity.");
            return;
        }

        boolean rs = isProductAlreadyAdded(productId);
        if (rs) {
            showAlert(Alert.AlertType.ERROR, "Duplicate Product", "This product is already added to the table.");
            return;
        }

        if (productDTO.getQuantity() < quantity) {
            showAlert(Alert.AlertType.ERROR, "Insufficient Stock", "Requested quantity exceeds available stock.");
            return;
        }
        productDTO.setQuantity(quantity);

        int mainProductId = checkForMainProductInCart();
        if (mainProductId > 0 && checkOfferAvailable(mainProductId, productId)) {
            productDTO.setDiscount("100");
            productDTO.setQuantity(1); // Assume the free product quantity is always 1
            showAlert(Alert.AlertType.INFORMATION, "Offer Applied", "Buy One Get One offer applied for this product!");
        }

        OrderItemTM newOrderItem = convertDTOtoTM(productDTO);
        observableOrderItems.add(newOrderItem);

        orderItems.add(String.valueOf(orderItemTMtolist(newOrderItem)));
        tblProducts.setItems(observableOrderItems);
//        updateTotals(productDTO);

        cmbProduct.getSelectionModel().clearSelection();
        txtQuantity.clear();
    }

    public void proceedOnClick(ActionEvent actionEvent) {
        try {
            int orderId = orderService.addOrder(collectOrderData());

            if (orderId > 0) {
                for (String orderItem : orderItems) {
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    String[] parts = orderItem.trim().replaceAll("[\\[\\]]", "").split(",");

                    orderItemDTO.setProductId(Integer.parseInt(parts[0].trim()));
                    orderItemDTO.setOrderId(orderId);
                    orderItemDTO.setQuantity(Integer.parseInt(parts[1].trim()));
                    orderItemDTO.setSellPrice(Double.parseDouble(parts[2].trim()));
                    boolean isInserted = orderService.addOrderItem(orderItemDTO);

                    if (!isInserted) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add order item for product ID: " + orderItemDTO.getProductId());
                     return; // Stop further processing
                    }
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Done!");

                observableOrderItems.clear();
                tblProducts.setItems(observableOrderItems);
                orderItems.clear();
                totalPrice = 0;
                DiscountPrice = 0;
                subtotal = 0;

            } else {
               showAlert(Alert.AlertType.ERROR, "Error", "Failed to create order.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Exception", "An error occurred: " + e.getMessage());
        }
    }

    private int getSelectedProductId() {
        Map<String, Integer> productMap = new HashMap<>();
        List<ProductDTO> products = productService.getAllname();
        if (products != null && !products.isEmpty()) {
            for (ProductDTO product : products) {
                cmbProduct.getItems().add(product.getId() + " - " + product.getName());
                productMap.put(product.getId() + " - " + product.getName(), product.getId()); // Use full name with ID as key
            }
        } else {
            throw new RuntimeException("No products found.");
        }

        // Enable editable ComboBox for search functionality
        cmbProduct.setEditable(true);

        // Add a listener for search functionality with auto-show
        cmbProduct.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            String typedText = newValue.toLowerCase();

            // Filter items dynamically
            List<String> filteredItems = products.stream()
                    .map(product -> product.getId() + " - " + product.getName())
                    .filter(item -> item.toLowerCase().contains(typedText))
                    .toList();

            // Update ComboBox items
            cmbProduct.getItems().clear();
            cmbProduct.getItems().addAll(filteredItems);

            // Keep the typed text intact
            cmbProduct.getEditor().setText(newValue);
            cmbProduct.getEditor().positionCaret(newValue.length());

            // Automatically show the dropdown
            if (!filteredItems.isEmpty()) {
                cmbProduct.show();
            }
        });

        // Return the selected product ID when requested
        String selectedName = String.valueOf(cmbProduct.getValue());
        if (selectedName != null && productMap.containsKey(selectedName)) {
            return productMap.get(selectedName);
        } else {
            return 0;
        }
    }

    public void visulizeTable(){
        colId.setCellValueFactory(new PropertyValueFactory<>("orderItemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colSellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        colAction.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Button button, boolean empty) {
                super.updateItem(button, empty);
                if (empty || getTableRow() == null) {
                    setGraphic(null);
                } else {
                    OrderItemTM orderItemTM = getTableRow().getItem();
                    if (orderItemTM != null) {
                        HBox actionButtons = new HBox(10, orderItemTM.getDeleteButton());
                        actionButtons.setAlignment(Pos.CENTER);
                        actionButtons.setStyle("-fx-padding: 1;");
                        setGraphic(actionButtons);
                    }
                }
            }
        });
    }

    private int checkForMainProductInCart() {
        List<Integer> mainProductIds = offerService.mainProductIds();
        for (OrderItemTM item : observableOrderItems) {
            if (mainProductIds.contains(item.getOrderItemId())) {
                return item.getOrderItemId();
            }
        }
        return -1;
    }

    private boolean checkOfferAvailable(Integer mainProductId, Integer productId) {
        List<Integer> freeproductIds = offerService.freeProductIds(mainProductId);
        for (int id : freeproductIds) {
            if (id == productId) {
                return true;
            }
        }return false;
    }

    private OrderItemTM convertDTOtoTM(ProductDTO productDTO) {
        OrderItemTM orderItemTM = new OrderItemTM();
        orderItemTM.setOrderItemId(productDTO.getId());
        orderItemTM.setItemName(productDTO.getName());
        orderItemTM.setQuantity(productDTO.getQuantity());
        orderItemTM.setPrice(productDTO.getPrice());
        if (productDTO.getDiscount() != null){
            Double Discount = Double.parseDouble(productDTO.getDiscount());
            if(Discount> MIN_DISCOUNT|| Discount<MAX_DISCOUNT){
                orderItemTM.setSellPrice(productDTO.getPrice() * (100 - Discount) / 100);
                }
        }else {
            orderItemTM.setSellPrice(productDTO.getPrice());
        }
        orderItemTM.setTotalPrice(orderItemTM.getSellPrice()*productDTO.getQuantity());

        Button deleteButton = new Button();
        Image deleteIcon = new Image("image/delete.png"); // Provide the correct path to your delete icon
        ImageView deleteIconView = new ImageView(deleteIcon);
        deleteIconView.setFitWidth(15);  // Set icon size
        deleteIconView.setFitHeight(15);
        deleteButton.setGraphic(deleteIconView);
        deleteButton.setOnAction(event -> deleteItem(orderItemTM,productDTO));
        orderItemTM.setDeleteButton(deleteButton);

        updateTotals(productDTO,orderItemTM);

        return orderItemTM;
    }

    private void deleteItem(OrderItemTM orderItemTM, ProductDTO productDTO) {
        observableOrderItems.remove(orderItemTM);
        orderItems.remove(String.valueOf(orderItemTMtolist(orderItemTM)));
        tblProducts.setItems(observableOrderItems);
        totalPrice -= productDTO.getPrice() * productDTO.getQuantity();
        subtotal -= orderItemTM.getTotalPrice();
        DiscountPrice = totalPrice - subtotal;
        productcount = observableOrderItems.size();

        txtProducts.setText(String.valueOf(productcount));
        txtTotalPrice.setText(String.valueOf(totalPrice));
        txtDiscount.setText(String.valueOf(DiscountPrice));
        txtSubtotal.setText(String.valueOf(subtotal));

    }

    private List<String> orderItemTMtolist(OrderItemTM orderItemTM) {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(orderItemTM.getOrderItemId()));
        list.add(String.valueOf(orderItemTM.getQuantity()));
        list.add(String.valueOf(orderItemTM.getTotalPrice()));
        return list;
    }

    private void updateTotals(ProductDTO productDTO,OrderItemTM orderItemTM) {
        productcount = observableOrderItems.size()+1;
        totalPrice += productDTO.getPrice() * productDTO.getQuantity();
        subtotal += orderItemTM.getTotalPrice();
        DiscountPrice = totalPrice - subtotal;

        txtProducts.setText(String.valueOf(productcount));
        txtTotalPrice.setText(String.valueOf(totalPrice));
        txtDiscount.setText(String.valueOf(DiscountPrice));
        txtSubtotal.setText(String.valueOf(subtotal));
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private OrderDTO collectOrderData(){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalItems(productcount);
        orderDTO.setTotalAmount(subtotal);
        orderDTO.setBillDate(date());
        orderDTO.setTotalDiscount(DiscountPrice);
        orderDTO.setCustomerNumber("+94"+txtNumber.getText());
        return orderDTO;
    }

    private void updateDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd   HH:mm");

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1), // Update every second
                        event -> {
                            txtDate.setText(LocalDateTime.now().format(formatter));
                        }
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        timeline.play(); // Start the timeline
    }
    public void LogoutOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?", ButtonType.YES , ButtonType.NO);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            Stage window = (Stage) mainPane.getScene().getWindow();
            window.close();

            Stage stage = new Stage();
            try {
                Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginForm.fxml")));
                Scene scene = new Scene(load);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, e+"Faild load Login please contact Developer").show();
            }
        }
    }
    
    public String date(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    
}
