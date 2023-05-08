import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    ArrayList<BankAccount> bankAccounts; // Stores bankAccounts
    private String userName; // Stores name of the user using the ATM
    private double userBalance; // Stores balance of the user using the ATM
    private int userNum; // Stores which user is using the ATM
    private int receiverNum; // Stores which user is receiving money
    private long userInput = 0; // Stores the number that the user inputs through button
    private boolean enableNumberButton = false; // Checks if user can press number buttons
    private boolean enableOptionButton = false; // Checks if user can press option buttons
    private boolean enableEnterButton = true; // Checks if user can press enter button
    private int optionNum = 5; // Stores number of Option buttons
    private int userState = 0; // Stores which state the user is currently in (serves as a flag)
    private float sizeRatio = 1.0f; // Store size of the window
    private int[] frameSize = {1000 , 800}; // Stores size(width, height) of the frame

    // Setting bounds and fonts for text area
    private void TextAreaSetting(JTextArea textArea, int xPos, int yPos, int width, int height)
    {
        textArea.setBounds((int)(sizeRatio * xPos), (int)(sizeRatio * yPos), (int)(sizeRatio * width), (int)(sizeRatio * height)); // Setting size of the screen
        textArea.setFont(new Font("Arial",Font.BOLD,(int)(20 * sizeRatio))); // Setting font of the screen
    }

    // Setting bounds and fonts for buttons
    private void ButtonSetting(JButton button, int xPos, int yPos, int width, int height)
    {
        button.setBounds((int)(sizeRatio * xPos), (int)(sizeRatio * yPos), (int)(sizeRatio * width), (int)(sizeRatio * height));
        button.setFont(new Font("Arial",Font.BOLD,(int)(23 * sizeRatio)));
    }

    private JTextArea screen; // Stores screen that displays all the texts
    // Stores size(width, height) of the screen
    // Calculated with respect to frame size
    // The ratio was set by trying different number of cases
    private int[] screenSize = {frameSize[0] / 2, (frameSize[1] / 16) * 5};

    // Stores position(x, y) of the screen
    // Position x was calculated as difference of the frame size and the screen size and divided by two so that the screen always positions in the middle
    // Position y was calculated as 1/8 of size of the frame
    private int[] screenPosition = {(frameSize[0] - screenSize[0]) / 2, frameSize[1] / 8};
    private JTextArea createScreen()
    {
        // Text area that shows all the instructions and messages
        JTextArea screen = new JTextArea("Please, insert your card and press ENTER...");

        TextAreaSetting(
                screen,
                screenPosition[0],
                screenPosition[1],
                screenSize[0],
                screenSize[1]
        );
        screen.setOpaque(true); // Should be set as opaque inorder to set background color
        screen.setBackground(Color.white); // change background color to white to show the screen
        int screenBorder = (int)(5 * sizeRatio);
        screen.setBorder(BorderFactory.createEmptyBorder(screenBorder, screenBorder, screenBorder, screenBorder)); // Setting border so that text is not too close to the screen

        return screen;
    }

    private JButton[] numberButtons; // Stores number buttons used to type numbers
    // Stores size(width, height) of number buttons
    // Width was set to (width of screen divided by three) so that 3 buttons always fit the size of the screen
    // Height was set as such way so the buttons always leave margin at the bottom of the screen
    private int[] numberButtonSize = {screenSize[0] / 3, (frameSize[1] - (screenPosition[1] + screenSize[1] + screenPosition[1])) / 4};
    private JButton[] createNumberButtons()
    {
        ImageIcon[] numberIcons = new ImageIcon[10]; // Array that stores number icons(0~9)
        for(int i = 0; i < 10; i++) // Assigning image for each number icons
        {
            String dir = "src/icons/" + i + ".png"; // Stores directory of each number icons from 0 to 9
            numberIcons[i] = new ImageIcon(dir); // Assign each icon to corresponding array index
        }

        JButton[] numberButtons = new JButton[10]; // Array of buttons that the user uses to input number

        // Setting position, size, image, and functionalities for each number button
        for(int i = 0; i < 10; i++)
        {
            numberButtons[i] = new JButton(numberIcons[i]); // Create number buttons with number icons on them

            if(i == 0)
            {
                // Set position of each button with respect to the size of the screen and button size for button 0
                ButtonSetting(
                        numberButtons[i],
                        screenPosition[0] + numberButtonSize[0],
                        screenPosition[1] + screenSize[1] + 3 * numberButtonSize[1],
                        numberButtonSize[0],
                        numberButtonSize[1]
                );
            }
            else
            {
                // Set position of each button with respect to the size of the screen and button size for button 1~9
                ButtonSetting(
                        numberButtons[i],
                        screenPosition[0] + ((i - 1) % 3) * numberButtonSize[0],
                        screenSize[1] + screenPosition[1] + ((i - 1)/ 3) * numberButtonSize[1],
                        numberButtonSize[0],
                        numberButtonSize[1]
                );
            }

            long finalI = i; // finalI was used because variable 'i' has to be referenced from within inner class

            // Create action listener for each number button
            // Should add corresponding number to 'userInput' variable
            numberButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Only works when the user can input number
                    // It becomes true when user has to input PIN, amount of money, or bank account
                    if(enableNumberButton)
                    {
                        String text = screen.getText(); // Stores original text on the screen
                        screen.setText(text + finalI); // Change the number shown on the screen
                        userInput = (userInput * 10) + finalI; // Add number to user input variable
                    }
                }
            });
        }

        return numberButtons;
    }

    private JButton cancelButton; // Stores cancel button that lets user go to the start screen
    private JButton createCancelButton()
    {
        ImageIcon cancelIcon = new ImageIcon("src/icons/cancel.png"); // Stores cancel icon
        JButton cancelButton = new JButton("Cancel", cancelIcon); // Create cancel button
        // Set position and size of cancel button using size and position of screen and number button so that they fit
        ButtonSetting(
                cancelButton,
                screenPosition[0] + 3 * numberButtonSize[0],
                screenPosition[1] + screenSize[1],
                (int)(numberButtonSize[0] * 1.2),
                numberButtonSize[1]
        );

        // Create action listener for cancel button
        // Should return to start menu
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // change userState, userInput, enableNumberButton, enableOptionButton, enableEnterButton to initial value
                userState = 0;
                userInput = 0;
                enableNumberButton = false;
                enableOptionButton = false;
                enableEnterButton = true;

                screen.setText("Please, insert your card and press ENTER...");  // set text to that of start screen
            }
        });

        return cancelButton;
    }

    private JButton clearButton;
    private JButton createClearButton()
    {
        ImageIcon clearIcon = new ImageIcon("src/icons/clear.png"); // Stores clear icon
        JButton clearButton = new JButton("Clear", clearIcon); // Create clear button
        // Set position and size of clear button using size and position of screen and number button so that they fit
        ButtonSetting(
                clearButton,
                screenPosition[0] + 3 * numberButtonSize[0],
                screenPosition[1] + screenSize[1] + numberButtonSize[1],
                (int)(numberButtonSize[0] * 1.2),
                numberButtonSize[1]
        );

        // Create action listener for clear button
        // Should clear number on the screen
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Only works when the user can input number
                if(enableNumberButton)
                {
                    userInput = 0; // Resets the user input

                    char[] texts = screen.getText().toCharArray(); // Get text on screen as character array
                    String result = ""; // Stores the final text

                    for(int i = 0; i < texts.length; i++)
                    {
                        if(!Character.isDigit(texts[i])) // Checks if the character (of text) is digit
                        {
                            result = result + texts[i]; // If not, add to result string
                        }
                    }

                    screen.setText(result); // Set text on the screen after deleting all the digit
                }
            }
        });

        return clearButton;
    }

    private JButton enterButton; // Stores enter button that gets user's input
    private JButton createEnterButton()
    {
        ImageIcon enterIcon = new ImageIcon("src/icons/enter.png"); // Stores enter icon
        JButton enterButton = new JButton("Enter", enterIcon); // Create enter button
        // Set position and size of enter button using size and position of screen and number button so that they fit
        ButtonSetting(
                enterButton,
                screenPosition[0] + 3 * numberButtonSize[0],
                screenPosition[1] + screenSize[1] + 2 * numberButtonSize[1],
                (int)(numberButtonSize[0] * 1.2),
                numberButtonSize[1]
        );

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(enableEnterButton)
                {
                    boolean checkAccount;
                    boolean checkPIN;
                    switch (userState)
                    {
                        case 0: // Start screen
                            userState = 1; // change user's state to PIN screen
                            enableNumberButton = true; // allows user to use number button
                            screen.setText("PIN: "); // changing text on the
                            break;

                        case 1: // PIN screen
                            checkPIN = false; // Checks if the user has entered the correct PIN
                            for(int i = 0; i < bankAccounts.size(); i++)
                            {
                                // Compares PIN code of bankAccount object and user's input
                                if(bankAccounts.get(i).getPinCode() == userInput)
                                {
                                    checkPIN = true;
                                    userNum = i;
                                    userName = bankAccounts.get(i).getUserName();
                                    userBalance = bankAccounts.get(i).getBalance();
                                }
                            }

                            if(checkPIN) // If user entered the correct PIN
                            {
                                userState = 2; // Change user's state to main screen
                                enableNumberButton = false; // Disable number button for main screen
                                enableOptionButton = true; // Enable option button as the user has to choose which option to use
                                enableEnterButton = false; // Disable enter button for main screen
                                screen.setText("Welcome " + userName +
                                        "\nPlease choose options:" +
                                        "\nOPTION 1: Balance Checking" +
                                        "\nOPTION 2: Withdrawing Money" +
                                        "\nOPTION 3: Deposit" +
                                        "\nOPTION 4: Transfer" +
                                        "\nOPTION 5: Change PIN");
                            }
                            else // If user entered the wrong PIN
                            {
                                screen.setText("Wrong PIN! Try Again.\nPIN: ");
                            }
                            userInput = 0; // Reset user input
                            break;

                        case 2: // Balance Checking screen
                            // change userState, userInput, enableNumberButton, and enableOptionButton to initial value
                            userState = 0; // Going back to PIN screen
                            userInput = 0;
                            enableNumberButton = false;
                            enableOptionButton = false;

                            // Set text to finish statement of balance checking screen
                            screen.setText("Thank you for banking with us!" +
                                    "\nPress ENTER...");
                            break;

                        case 3: // Withdraw Screen
                            if(userInput <= userBalance) // If user's input is less than or equal to the user's balance
                            {
                                double resultBalance = userBalance - (double)userInput;
                                bankAccounts.get(userNum).setBalance(resultBalance); // Change user's balance by subtracting user's input
                                screen.setText("Success :)" +
                                        "\nUser: " + userName +
                                        "\nWithdrawal Amount: " + String.format("%.2f", (double)userInput) +
                                        "\nCurrent Balance: " + String.format("%.2f", resultBalance) +
                                        "\nPress ENTER...");
                            }
                            else // If user's input is greater than the user's balance
                            {
                                userState = 0; // Going back to PIN screen
                                screen.setText("Not enough money!" +
                                        "\nPlease ENTER...");
                            }
                            enableNumberButton = false; // disable number button
                            userInput = 0; // Reset user input
                            userState = 0; // Going back to PIN screen
                            break;

                        case 4: // Deposit Screen
                            bankAccounts.get(userNum).setBalance(userBalance + (double)userInput); // Change user's balance by subtracting user's input
                            screen.setText("Success :)" +
                                    "\nUser: " + userName +
                                    "\nDeposit Amount: " + String.format("%.2f", (double)userInput) +
                                    "\nCurrent Balance: " + String.format("%.2f", (userBalance + (double)userInput)) +
                                    "\nPress ENTER...");
                            enableNumberButton = false; // disable number button
                            userInput = 0; // Reset user input
                            userState = 0; // Going back to PIN screen
                            break;

                        case 5: // Transfer screen (Getting receiver)
                            checkAccount = false; // Checks if the user has entered the correct account number
                            for(int i = 0; i < bankAccounts.size(); i++)
                            {
                                // Compares PIN code of bankAccount object and user's input
                                if(bankAccounts.get(i).getBankNumber().equals(Long.toString(userInput)))
                                {
                                    checkAccount = true;
                                    receiverNum = i;
                                }
                            }

                            if(checkAccount) // If such account exists
                            {
                                userState = 6; // Going to transfer screen (Getting Amount)
                                screen.setText("Transfer Account: " + bankAccounts.get(receiverNum).getUserName() +
                                        "\nEnter Transfer Amount: ");
                            }
                            else
                            {
                                screen.setText("You entered the wrong account number!" +
                                        "\nPress ENTER...");
                                userState = 0; // Going back to PIN Screen
                            }
                            userInput = 0; // Reset user input
                            break;

                        case 6: // Transfer screen (Getting amount of money)
                            if(userInput <= userBalance) // If user's input is less than or equal to the user's balance
                            {
                                bankAccounts.get(userNum).setBalance(userBalance - (double)userInput); // Change user's balance by subtracting user's input
                                bankAccounts.get(receiverNum).setBalance(bankAccounts.get(receiverNum).getBalance() + (double)userInput); // Increase receiver's balance
                                screen.setText("Transfer Amount: " + String.format("%.2f", (double)userInput) +
                                        "\nCurrent Balance: " + String.format("%.2f", (userBalance - (double)userInput)) +
                                        "\nPress ENTER...");
                            }
                            else // If user's input is greater than the user's balance
                            {
                                userState = 0; // Going back to PIN screen
                                screen.setText("Not enough money!" +
                                        "\nPlease ENTER...");
                            }
                            enableNumberButton = false; // disable number button
                            userInput = 0; // Reset user input
                            userState = 0; // Going back to PIN screen
                            break;

                        case 7: // Current PIN screen
                            checkPIN = false;
                            if(bankAccounts.get(userNum).getPinCode() == userInput)
                            {
                                screen.setText("New PIN(4 ~ 8 digits): ");
                                userState = 8; // Going to set PIN screen
                            }
                            else
                            {
                                screen.setText("Wrong PIN! Try Again.\nCurrent PIN: ");
                            }
                            userInput = 0;
                            break;

                        case 8: // New PIN screen
                            if(999 < userInput && userInput < 100000000) // Validation check for user's PIN
                            {
                                // Set text to finish statement of new PIN screen
                                screen.setText("Successfully changed PIN number!" +
                                        "\nPress ENTER...");
                                bankAccounts.get(userNum).setPinCode((int)userInput);
                                userState = 0;
                            }
                            else
                            {
                                // Set text to validation check message
                                screen.setText("PIN should be between 4 and 8 digits" +
                                        "New PIN(4 ~ 8 digits): ");
                            }
                            break;
                    }
                }
            }
        });

        return enterButton;
    }

    private JButton[] optionButtons; // Store option buttons (Balance check, Withdrawal, Deposit, Transfer)
    private JButton[] createOptionButtons()
    {
        JButton[] optionButtons = new JButton[5];

        // --------------------------- Balance Checking Button ---------------------------------
        ImageIcon arrowIcon = new ImageIcon("src/icons/arr.png"); // Stores arrow icon
        optionButtons[0] = new JButton("OPTION 1", arrowIcon); // Create balance checking button
        // Set position and size of balance checking button
        // Position x was calculated as 1/16 of size of the frame
        // Position y was calculated as 1/8 of size of the frame + n * height of option button for y position
        // Width was calculated as difference between position of the screen and the position of option button so that the button fit exactly
        // Height was calculated as the height of screen divided by 4 to fit exactly to the size of screen
        ButtonSetting(
                optionButtons[0],
                frameSize[1] / 16,
                frameSize[1] / 8,
                screenPosition[0] - (frameSize[1] / 16),
                screenSize[1] / optionNum
        );

        // Create action listener for balance check button
        // Should display name of the user and the user's balance
        optionButtons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(enableOptionButton) // Only works when option button is enabled
                {
                    userState = 2; // Change user's state to balance checking screen
                    enableEnterButton = true; // Enable enter button
                    enableOptionButton = false; // Disable option button
                    screen.setText("User: " + userName +
                            "\nBalance: " + String.format("%.2f", userBalance) +
                            "\nPress Enter...");
                }
            }
        });

        // --------------------------- Withdraw Button ---------------------------------
        optionButtons[1] = new JButton("OPTION 2", arrowIcon); // Create withdraw button
        // Set position and size of withdraw button
        // Position x was calculated as 1/16 of size of the frame
        // Position y was calculated as 1/8 of size of the frame + n * height of option button for y position
        // Width was calculated as difference between position of the screen and the position of option button so that the button fit exactly
        // Height was calculated as the height of screen divided by 4 to fit exactly to the size of screen
        ButtonSetting(
                optionButtons[1],
                frameSize[1] / 16,
                (frameSize[1] / 8) + (screenSize[1] / optionNum),
                screenPosition[0] - (frameSize[1] / 16),
                screenSize[1] / optionNum
        );

        // Create action listener for withdraw button
        // Should display statement asking for withdrawal amount
        optionButtons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(enableOptionButton) // Only works when option button is enabled
                {
                    userState = 3; // Change user's state to withdraw screen
                    enableOptionButton = false; // Disable option button
                    enableEnterButton = true; // Enable enter button
                    enableNumberButton = true; // Enable number button as the user has to enter amount of money

                    screen.setText("Enter Withdrawal Amount: ");
                }
            }
        });

        // --------------------------- Deposit Button ---------------------------------
        optionButtons[2] = new JButton("OPTION 3", arrowIcon); // Create deposit button
        // Set position and size of deposit button
        // Position x was calculated as 1/16 of size of the frame
        // Position y was calculated as 1/8 of size of the frame + n * height of option button for y position
        // Width was calculated as difference between position of the screen and the position of option button so that the button fit exactly
        // Height was calculated as the height of screen divided by 4 to fit exactly to the size of screen
        ButtonSetting(
                optionButtons[2],
                frameSize[1] / 16,
                (frameSize[1] / 8) + (screenSize[1] / optionNum) * 2,
                screenPosition[0] - (frameSize[1] / 16),
                screenSize[1] / optionNum
        );

        // Create action listener for deposit button
        // Should display statement asking for deposit amount
        optionButtons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(enableOptionButton) // Only works when option button is enabled
                {
                    userState = 4; // Change user's state to deposit screen
                    enableOptionButton = false; // Disable option button
                    enableEnterButton = true; // Enable enter button
                    enableNumberButton = true; // Enable number button as the user has to enter amount of money

                    screen.setText("Enter Deposit Amount: ");
                }
            }
        });

        // --------------------------- Transfer Button ---------------------------------
        optionButtons[3] = new JButton("OPTION 4", arrowIcon); // Create transfer button
        // Set position and size of transfer button
        // Position x was calculated as 1/16 of size of the frame
        // Position y was calculated as 1/8 of size of the frame + n * height of option button for y position
        // Width was calculated as difference between position of the screen and the position of option button so that the button fit exactly
        // Height was calculated as the height of screen divided by 4 to fit exactly to the size of screen
        ButtonSetting(
                optionButtons[3],
                frameSize[1] / 16,
                (frameSize[1] / 8) + (screenSize[1] / optionNum) * 3,
                screenPosition[0] - (frameSize[1] / 16),
                screenSize[1] / optionNum
        );

        // Create action listener for transfer button
        // Should ask for receiver's account number
        optionButtons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(enableOptionButton) // Only works when option button is enabled
                {
                    userState = 5; // Change user's state to transfer screen
                    enableOptionButton = false; // Disable option button
                    enableEnterButton = true; // Enable enter button
                    enableNumberButton = true; // Enable number button as the user has to enter amount of money

                    screen.setText("Enter Account Number(Receiver): ");
                }
            }
        });

        // --------------------------- Change PIN Button ---------------------------------

        optionButtons[4] = new JButton("OPTION 5", arrowIcon); // Create change PIN button
        // Set position and size of change PIN button
        // Position x was calculated as 1/16 of size of the frame
        // Position y was calculated as 1/8 of size of the frame + n * height of option button for y position
        // Width was calculated as difference between position of the screen and the position of option button so that the button fit exactly
        // Height was calculated as the height of screen divided by 4 to fit exactly to the size of screen
        ButtonSetting(
                optionButtons[4],
                frameSize[1] / 16,
                (frameSize[1] / 8) + (screenSize[1] / optionNum) * 4,
                screenPosition[0] - (frameSize[1] / 16),
                screenSize[1] / optionNum
        );

        // Create action listener for change PIN button
        // Should ask for account number, current PIN and changed PIN
        optionButtons[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(enableOptionButton) // Only works when option button is enabled
                {
                    userState = 7; // Change user's state to transfer screen
                    enableOptionButton = false; // Disable option button
                    enableEnterButton = true; // Enable enter button
                    enableNumberButton = true; // Enable number button as the user has to enter amount of money

                    screen.setText("Current PIN: ");
                }
            }
        });

        return optionButtons;
    }

    private JButton engButton; // Store button for changing text to English
    private JButton createEngButton()
    {
        ImageIcon engIcon = new ImageIcon("src/icons/eng.png"); // Stores English icon
        JButton engButton = new JButton("ENGLISH", engIcon); // Create English button
        // Set position and size of English button
        // Position x was set as the end of the screen
        // Position y was calculated as 1/8 of size of the frame + n * height of option button for y position
        // Width was calculated to be same as that of enter button
        // Height was calculated to be same as that of option button
        ButtonSetting(
                engButton,
                screenPosition[0] + screenSize[0],
                frameSize[1] / 8,
                (int)(numberButtonSize[0] * 1.2),
                screenSize[1] / 4
        );

        return engButton;
    }

    private JButton korButton; // Store button for changing text to Korean
    private JButton createKorButton()
    {
        ImageIcon korIcon = new ImageIcon("src/icons/kor.png"); // Stores Korean icon
        JButton korButton = new JButton("KOREAN", korIcon); // Create Korean button
        // Set position and size of Korean button
        // Position x was set as the end of the screen
        // Position y was calculated as 1/8 of size of the frame + n * height of option button for y position
        // Width was calculated to be same as that of enter button
        // Height was calculated to be same as that of option button
        ButtonSetting(
                korButton,
                screenPosition[0] + screenSize[0],
                frameSize[1] / 8 + (screenSize[1] / 4),
                (int)(numberButtonSize[0] * 1.2),
                screenSize[1] / 4
        );

        return korButton;
    }

    private JButton sizeUpButton; // stores button that sizes up the hole frame
    private JButton sizeDownButton;
    private JButton createSizeUpButton()
    {
        JButton sizeUpButton = new JButton("+"); // Create size up button
        // Set font and bounds for size up button
        ButtonSetting(
                sizeUpButton,
                frameSize[1] / 16,
                screenSize[1] + screenPosition[1] + (int)(1.5 * numberButtonSize[1]),
                ((screenPosition[0] - (frameSize[1] / 16)) / 3) * 2,
                ((screenSize[1] / 4) / 3) * 2
        );

        // Create action listener for size up button
        // Should increase the size of all the elements in the frame
        sizeUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sizeRatio <= 1.6) {
                    sizeRatio += 0.2; // Increase the ratio by 0.2 only when the ratio is smaller than 1.8
                }

                setSize((int)(sizeRatio * frameSize[0]), (int)(sizeRatio * frameSize[1]));

                // [Explanations for codes below]
                // Re-call functions used for setting fonts and bounds for each textArea and button
                // Simply copy-and-pasted from each button settings
                TextAreaSetting(screen, screenPosition[0], screenPosition[1], screenSize[0], screenSize[1]);
                for(int i = 0; i < 10; i++) {
                    if (i == 0) {
                        ButtonSetting(numberButtons[i], screenPosition[0] + numberButtonSize[0], screenPosition[1] + screenSize[1] + 3 * numberButtonSize[1], numberButtonSize[0], numberButtonSize[1]);
                    } else {
                        ButtonSetting(numberButtons[i], screenPosition[0] + ((i - 1) % 3) * numberButtonSize[0], screenSize[1] + screenPosition[1] + ((i - 1) / 3) * numberButtonSize[1], numberButtonSize[0], numberButtonSize[1]);
                    }
                }
                ButtonSetting(cancelButton, screenPosition[0] + 3 * numberButtonSize[0], screenPosition[1] + screenSize[1], (int)(numberButtonSize[0] * 1.2), numberButtonSize[1]);
                ButtonSetting(clearButton, screenPosition[0] + 3 * numberButtonSize[0], screenPosition[1] + screenSize[1] + numberButtonSize[1], (int)(numberButtonSize[0] * 1.2), numberButtonSize[1]);
                ButtonSetting(enterButton, screenPosition[0] + 3 * numberButtonSize[0], screenPosition[1] + screenSize[1] + 2 * numberButtonSize[1], (int)(numberButtonSize[0] * 1.2), numberButtonSize[1]);
                for(int i = 0; i< optionNum; i++)
                {
                    ButtonSetting(optionButtons[i], frameSize[1] / 16   , (frameSize[1] / 8) + (screenSize[1] / optionNum) * i, screenPosition[0] - (frameSize[1] / 16), screenSize[1] / optionNum);
                }
                ButtonSetting(engButton, screenPosition[0] + screenSize[0], frameSize[1] / 8, (int)(numberButtonSize[0] * 1.2), screenSize[1] / 4);
                ButtonSetting(korButton, screenPosition[0] + screenSize[0], frameSize[1] / 8 + (screenSize[1] / 4), (int)(numberButtonSize[0] * 1.2), screenSize[1] / 4);
                ButtonSetting(sizeUpButton, frameSize[1] / 16, screenSize[1] + screenPosition[1] + (int)(1.5 * numberButtonSize[1]), ((screenPosition[0] - (frameSize[1] / 16)) / 3) * 2, ((screenSize[1] / 4) / 3) * 2);
                ButtonSetting(sizeDownButton, frameSize[1] / 16, screenSize[1] + screenPosition[1] + 2 * numberButtonSize[1], ((screenPosition[0] - (frameSize[1] / 16))
                        / 3) * 2, ((screenSize[1] / 4) / 3) * 2);
            }
        });

        return sizeUpButton;
    }

    private JButton createSizeDownButton()
    {
        JButton sizeDownButton = new JButton("-"); // Create size up button
        // Set font and bounds for size down button
        ButtonSetting(
                sizeDownButton,
                frameSize[1] / 16,
                screenSize[1] + screenPosition[1] + 2 * numberButtonSize[1],
                ((screenPosition[0] - (frameSize[1] / 16)) / 3) * 2,
                ((screenSize[1] / 4) / 3) * 2
        );

        // Create action listener for size down button
        // Should decrease the size of all the elements in the frame
        sizeDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sizeRatio >= 1) {
                    sizeRatio -= 0.2; // Decrease the ratio by 0.2 only when the ratio is bigger than 0.8
                }

                setSize((int)(sizeRatio * frameSize[0]), (int)(sizeRatio * frameSize[1]));

                // [Explanations for codes below]
                // Re-call functions used for setting fonts and bounds for each textArea and button
                // Simply copy-and-pasted from each button settings
                TextAreaSetting(screen, screenPosition[0], screenPosition[1], screenSize[0], screenSize[1]);
                for(int i = 0; i < 10; i++) {
                    if (i == 0) {
                        ButtonSetting(numberButtons[i], screenPosition[0] + numberButtonSize[0], screenPosition[1] + screenSize[1] + 3 * numberButtonSize[1], numberButtonSize[0], numberButtonSize[1]);
                    } else {
                        ButtonSetting(numberButtons[i], screenPosition[0] + ((i - 1) % 3) * numberButtonSize[0], screenSize[1] + screenPosition[1] + ((i - 1) / 3) * numberButtonSize[1], numberButtonSize[0], numberButtonSize[1]);
                    }
                }
                ButtonSetting(cancelButton, screenPosition[0] + 3 * numberButtonSize[0], screenPosition[1] + screenSize[1], (int)(numberButtonSize[0] * 1.2), numberButtonSize[1]);
                ButtonSetting(clearButton, screenPosition[0] + 3 * numberButtonSize[0], screenPosition[1] + screenSize[1] + numberButtonSize[1], (int)(numberButtonSize[0] * 1.2), numberButtonSize[1]);
                ButtonSetting(enterButton, screenPosition[0] + 3 * numberButtonSize[0], screenPosition[1] + screenSize[1] + 2 * numberButtonSize[1], (int)(numberButtonSize[0] * 1.2), numberButtonSize[1]);
                for(int i = 0; i< optionNum; i++)
                {
                    ButtonSetting(optionButtons[i], frameSize[1] / 16, (frameSize[1] / 8) + (screenSize[1] / 4) * i, screenPosition[0] - (frameSize[1] / 16), screenSize[1] / 4);
                }
                ButtonSetting(engButton, screenPosition[0] + screenSize[0], frameSize[1] / 8, (int)(numberButtonSize[0] * 1.2), screenSize[1] / 4);
                ButtonSetting(korButton, screenPosition[0] + screenSize[0], frameSize[1] / 8 + (screenSize[1] / 4), (int)(numberButtonSize[0] * 1.2), screenSize[1] / 4);
                ButtonSetting(sizeUpButton, frameSize[1] / 16, screenSize[1] + screenPosition[1] + (int)(1.5 * numberButtonSize[1]), ((screenPosition[0] - (frameSize[1] / 16)) / 3) * 2, ((screenSize[1] / 4) / 3) * 2);
                ButtonSetting(sizeDownButton, frameSize[1] / 16, screenSize[1] + screenPosition[1] + 2 * numberButtonSize[1], ((screenPosition[0] - (frameSize[1] / 16)) / 3) * 2, ((screenSize[1] / 4) / 3) * 2);
            }
        });

        return sizeDownButton;
    }

    // Constructor for the main frame
    public MainFrame(ArrayList<BankAccount> bankAccounts)
    {
        this.bankAccounts = bankAccounts;

        screen = createScreen();
        add(screen); // add screen(JLabel object) to frame

        numberButtons = createNumberButtons();
        for(int i = 0; i < 10; i++)
        {
            add(numberButtons[i]); // Add each number button to screen
        }

        cancelButton = createCancelButton();
        add(cancelButton); // Add cancel button to frame

        clearButton = createClearButton();
        add(clearButton); // Add clear button to frame

        enterButton = createEnterButton();
        add(enterButton); // Add enter button to frame

        optionButtons = createOptionButtons();
        for(int i = 0; i < optionNum; i++)
        {
            add(optionButtons[i]); // Add option buttons to frame
        }

        engButton = createEngButton();
        add(engButton); // Add English button to frame

        korButton = createKorButton();
        add(korButton); // Add Korean button to frame

        sizeUpButton = createSizeUpButton();
        add(sizeUpButton);

        sizeDownButton = createSizeDownButton();
        add(sizeDownButton);

        ImageIcon wooriIcon = new ImageIcon("src/icons/woori.png"); // Get woori bank Icon
        Image img = wooriIcon.getImage(); // Change its data type to img to modify the size
        // Modify size of the icon with reference to screen size
        img = img.getScaledInstance(
                ((screenSize[0] / 3) * 2),
                ((screenPosition[1] / 3) * 2),
                Image.SCALE_SMOOTH
        );
        wooriIcon = new ImageIcon(img); // Change its data type back to ImageIcon

        JLabel wooriLogo = new JLabel(wooriIcon); // Create label for displaying woori icon
        // Set position of Woori Icon with respect to frame size, screen size, and screen position
        wooriLogo.setBounds(
                frameSize[1] / 16,
                frameSize[1] / 60,
                ((screenSize[0] / 3) * 2),
                ((screenPosition[1] / 3) * 2)
        );
        add(wooriLogo); // Add Woori Image to frame

        // Settings for frame
        setTitle("ATM");
        setSize(frameSize[0], frameSize[1]);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
    }
}
