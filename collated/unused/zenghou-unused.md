# zenghou-unused
###### /java/seedu/address/ui/MainWindow.java
``` java
    /**
     * Fills up all the commandbox placeholder of this window.
     */
    void fillCommandBoxAndDisplayPanel() {
        CommandBox commandBox = new CommandBox(logic);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());
    }
```
