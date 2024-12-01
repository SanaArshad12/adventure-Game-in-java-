import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Player {
    String name;
    int strength;
    int intelligence;
    int health;
    int stamina;
    int experience;
    List<String> inventory;

    public Player(String name) {
        this.name = name;
        this.strength = 5;
        this.intelligence = 5;
        this.health = 10;
        this.stamina = 10;
        this.experience = 0;
        this.inventory = new ArrayList<>();
    }

    public void addItem(String item) {
        inventory.add(item);
        switch (item) {
            case "Torch":
                strength += 2;
                break;
            case "Ancient Book":
                intelligence += 3;
                break;
            case "Potion":
                health += 5;
                break;
            case "Stamina Boost":
                stamina += 5;
                break;
        }
    }

    public void useItem(String item) {
        if (inventory.contains(item)) {
            switch (item) {
                case "Potion":
                    health += 5;
                    break;
                case "Stamina Boost":
                    stamina += 5;
                    break;
            }
            inventory.remove(item);
        }
    }

    public String getInventory() {
        return inventory.isEmpty() ? "Empty" : String.join(", ", inventory);
    }

    public String getStats() {
        return "Strength: " + strength + " | Intelligence: " + intelligence + " | Health: " + health + " | Stamina: " + stamina + " | XP: " + experience;
    }

    public void gainExperience(int points) {
        experience += points;
        if (experience >= 10) {
            levelUp();
        }
    }

    private void levelUp() {
        strength += 1;
        intelligence += 1;
        health += 2;
        stamina += 2;
        experience = 0; // Reset XP after leveling up
    }

    // Attack method added to reduce enemy health
    public void attack(Enemy enemy) {
        int damage = (int) (Math.random() * strength); // Random damage based on player strength
        enemy.health -= damage;
        if (enemy.health < 0) enemy.health = 0; // Ensure enemy health doesn't go below zero
    }
}

class Enemy {
    String name;
    int health;
    int strength;

    public Enemy(String name, int health, int strength) {
        this.name = name;
        this.health = health;
        this.strength = strength;
    }

    // Enemy can attack player
    public void attack(Player player) {
        int damage = (int) (Math.random() * strength); // Random damage based on enemy strength
        player.health -= damage;
        if (player.health < 0) player.health = 0; // Ensure player health doesn't go below zero
    }
}

class InteractiveStoryGameWithImages {
    private JFrame frame;
    private JLabel storyLabel;
    private JLabel imageLabel;
    private JPanel buttonPanel;
    private JLabel inventoryLabel;
    private JLabel statsLabel;
    private Player player;
    private Enemy enemy;
    private Random random;

    public InteractiveStoryGameWithImages() {
        random = new Random();
        setupUI();
    }

    private void setupUI() {
        frame = new JFrame("Interactive Story Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(40, 40, 40));

        inventoryLabel = new JLabel("Inventory: Empty");
        inventoryLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        inventoryLabel.setForeground(new Color(200, 200, 200));

        statsLabel = new JLabel("Stats: Strength: 5 | Intelligence: 5 | Health: 10 | Stamina: 10");
        statsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statsLabel.setForeground(new Color(200, 200, 200));

        headerPanel.add(inventoryLabel);
        headerPanel.add(statsLabel);

        // Story and Image Panel
        JPanel storyImagePanel = new JPanel(new BorderLayout());
        storyImagePanel.setBackground(new Color(30, 30, 30));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        storyLabel = new JLabel("<html>Welcome to the Interactive Story Game!<br>Enter your name to begin:</html>");
        storyLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        storyLabel.setForeground(new Color(230, 230, 230));
        storyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        storyImagePanel.add(imageLabel, BorderLayout.CENTER);
        storyImagePanel.add(storyLabel, BorderLayout.SOUTH);

        // Button Panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(new Color(40, 40, 40));

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(storyImagePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        startGame();
        frame.setVisible(true);
    }

    private void startGame() {
        buttonPanel.removeAll();
        JTextField nameField = new JTextField(15);
        JButton submitButton = createButton("Submit");

        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                player = new Player(name);
                updateStatsAndInventory();
                displayStory1();
            } else {
                storyLabel.setText("<html>Name cannot be empty. Please try again.</html>");
            }
        });

        buttonPanel.add(nameField);
        buttonPanel.add(submitButton);
        frame.revalidate();
        frame.repaint();
    }

    private void displayStory1() {
        storyLabel.setText("<html>You wake up in a dark cave. There's a torch on the wall.<br>You hear faint footsteps approaching.<br>What do you do?</html>");
        setImage("pic1.png");
        buttonPanel.removeAll();

        JButton takeTorch = createButton("Take the torch");
        JButton hide = createButton("Hide in the shadows");

        takeTorch.addActionListener(e -> {
            player.addItem("Torch");
            updateStatsAndInventory();
            displayStory2("torch");
        });

        hide.addActionListener(e -> displayStory2("hide"));

        buttonPanel.add(takeTorch);
        buttonPanel.add(hide);
        frame.revalidate();
        frame.repaint();
    }

    private void displayStory2(String decision) {
        if (decision.equals("torch")) {
            storyLabel.setText("<html>With the torch in hand, you scare off the footsteps.<br>You find an ancient book on a pedestal.<br>Do you take it?</html>");
            setImage("pic2.png");
        } else {
            storyLabel.setText("<html>You hide and the footsteps fade away.<br>You find an ancient book on a pedestal.<br>Do you take it?</html>");
            setImage("pic3.png");
        }

        buttonPanel.removeAll();

        JButton takeBook = createButton("Take the book");
        JButton leaveBook = createButton("Leave the book");

        takeBook.addActionListener(e -> {
            player.addItem("Ancient Book");
            player.gainExperience(5); // Gain XP for taking the book
            updateStatsAndInventory();
            startCombat(); // Start combat after taking the book
        });

        leaveBook.addActionListener(e -> startCombat()); // Start combat if book is left

        buttonPanel.add(takeBook);
        buttonPanel.add(leaveBook);
        frame.revalidate();
        frame.repaint();
    }

    private void startCombat() {
        // Creating a random enemy for combat
        enemy = new Enemy("Goblin", 20, 3);

        storyLabel.setText("<html>A wild " + enemy.name + " appears! Do you want to fight?</html>");
        setImage("pic4.png");

        buttonPanel.removeAll();

        JButton fightButton = createButton("Fight");
        fightButton.addActionListener(e -> {
            player.attack(enemy);
            if (enemy.health <= 0) {
                storyLabel.setText("<html>You defeated the " + enemy.name + "!</html>");
                player.gainExperience(10);
                updateStatsAndInventory();
                endCombat();
            } else {
                enemy.attack(player);
                if (player.health <= 0) {
                    storyLabel.setText("<html>You were defeated by the " + enemy.name + "...</html>");
                    endCombat();
                } else {
                    startCombat(); // Continue combat if both are still alive
                }
            }
        });

        JButton fleeButton = createButton("Flee");
        fleeButton.addActionListener(e -> {
            storyLabel.setText("<html>You successfully flee from the " + enemy.name + ".</html>");
            endCombat();
        });

        buttonPanel.add(fightButton);
        buttonPanel.add(fleeButton);
        frame.revalidate();
        frame.repaint();
    }

    private void endCombat() {
        buttonPanel.removeAll();
        JButton continueButton = createButton("Continue");
        continueButton.addActionListener(e -> {
            storyLabel.setText("<html>What do you want to do next?</html>");
            updateStatsAndInventory();
            frame.revalidate();
            frame.repaint();
        });
        buttonPanel.add(continueButton);
        frame.revalidate();
        frame.repaint();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.PLAIN, 18));
        button.setBackground(new Color(80, 80, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    private void setImage(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath); // Make sure these images exist
        imageLabel.setIcon(icon);
    }

    private void updateStatsAndInventory() {
        inventoryLabel.setText("Inventory: " + player.getInventory());
        statsLabel.setText(player.getStats());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InteractiveStoryGameWithImages::new);
    }
}
