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
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * @author Benjamin P. Jung
 */
public class DBusAnalyzer extends Application {

    private ResourceBundle messages;

    @Override
    public void init() throws Exception {
        super.init();
        this.messages = ResourceBundle.getBundle(getClass().getName());
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        primaryStage.setTitle(messages.getString("app.title"));
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(360);
        final Scene scene = new Scene(new RootPane(this));

        primaryStage.show();

    }

    public static void main(String... args) {
        Application.launch(DBusAnalyzer.class, args);
    }

}
