import javax.swing.*;

public class MainFrame extends JFrame
{
  public static final int WIDTH = 400;
  public static final int HEIGHT = 550;
 public MainFrame()
 {
   setSize (WIDTH, HEIGHT);
   setTitle ("Creatinine Calculator");
   MainPanel panel = new MainPanel(WIDTH, HEIGHT);
   add (panel);
   setVisible (true);
   setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
 }
}