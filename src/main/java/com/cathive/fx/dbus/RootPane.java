/*
 * Copyright (C) 2013 The Cat Hive Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cathive.fx.dbus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Benjamin P. Jung
 */
public class RootPane extends AnchorPane
                      implements Initializable {

    private DBusAnalyzer app;

    @FXML
    private BorderPane borderPane;

    public RootPane() {
        super();
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> aClass) {
                return RootPane.this;
            }
        });
        fxmlLoader.setResources(ResourceBundle.getBundle(DBusAnalyzer.class.getName()));
        fxmlLoader.setLocation(getClass().getResource("RootPane.fxml"));
        try {
            fxmlLoader.load();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RootPane(final DBusAnalyzer app) {
        this();
        this.app = app;
    }

    @FXML
    protected void showAboutDialog(final ActionEvent e) {

    }

    @Override
    public void initialize(URL location, ResourceBundle messages) {
    }

}
