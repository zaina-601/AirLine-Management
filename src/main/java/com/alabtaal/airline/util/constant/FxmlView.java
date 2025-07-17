package com.alabtaal.airline.util.constant;

public enum FxmlView {
    ITEMS{
        @Override
        public String getFilePath(){
            return "/fxml/frontPage.fxml";
        }
        @Override
        public String getTitle(){
            return "Welcome to Airline Management Project";
        }

    };
    public abstract String getFilePath();
    public abstract String getTitle();

}

