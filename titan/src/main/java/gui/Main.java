package gui;

import java.io.IOException;

import gui.Controller;
import gui.View;

public class Main {

    public static void main(String[] args) throws IOException {

    	View view = new View();
    	new Controller(view);


    }
}
