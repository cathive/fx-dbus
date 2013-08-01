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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.freedesktop.dbus.BusType;
import org.freedesktop.dbus.Connection;
import org.freedesktop.dbus.DBus;

import java.util.ResourceBundle;

/**
 * @author Benjamin P. Jung
 */
public class DBusAnalyzer extends Application {

    private ResourceBundle messages;

    private RootPane rootPane;

    // D-Bus sessions
    private Connection sessionBusConection;
    private Connection systemBusConnection;

    @Override
    public void init() throws Exception {

        super.init();

        DBus.initialize();
        this.sessionBusConection = Connection.getConnection(BusType.SESSION, true);
        this.systemBusConnection = Connection.getConnection(BusType.SYSTEM, true);

        this.messages = ResourceBundle.getBundle(getClass().getName());
        this.rootPane = new RootPane();

    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        primaryStage.setTitle(messages.getString("app.title"));
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(360);

        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setRoot(rootPane);
        fxmlLoader.setResources(ResourceBundle.getBundle(DBusAnalyzer.class.getName()));
        fxmlLoader.setLocation(getClass().getResource("RootPane.fxml"));
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> aClass) {
                if (aClass.equals(RootPane.class)) {
                    return rootPane;
                }
                else throw new IllegalArgumentException("No controller for class " + aClass.getName() + " found.");
            }
        });
        fxmlLoader.load();

        final Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        DBus.shutdown();
        super.stop();
    }

    public static void main(String... args) {
        Application.launch(DBusAnalyzer.class, args);
    }

}
