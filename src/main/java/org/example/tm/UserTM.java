package org.example.tm;

import javafx.scene.control.Button;
import lombok.Data;
import lombok.Getter;

import java.awt.*;

@Data

public class UserTM {
    private int id;
    private String name;
    private String username;
    private String email;
    private String role;
    private Button button;
    private Button buttonEdit;

}

