import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class MainPanel extends JPanel implements ActionListener
{
  DecimalFormat f = new DecimalFormat ("##.00");
  JTextField weightField;
  JTextField ageField;
  JTextField creatinineField;
  JComboBox weightBox;
  JComboBox sexBox;
  JComboBox creatinineBox;
  JTextArea resultsArea;
  String [] weightUnits = new String []{"kg","lb"};
  String [] creatinineUnits = new String []{((char)181) + "mol/L", "mg/dL"};
  String [] sexUnits = new String[]{"male", "female"};
  public MainPanel (int width, int height)
  {
    setSize (width, height);
    //setBackgroundColor (Color.RED);
    JLabel blankLabel = new JLabel ();
    JLabel titleLabel = new JLabel ("Creatinine Clearance Calculator");
    JLabel weightLabel = new JLabel ("Weight: ");
    JLabel sexLabel = new JLabel ("Sex: ");
    JLabel ageLabel = new JLabel ("Age: ");
    JLabel creatinineLabel = new JLabel ("Creatinine: ");
    //restrict these to only accept numbers
    weightField = new JTextField (10);
    ageField = new JTextField (10);
    creatinineField = new JTextField (10);
    //Fill in with options
    weightBox = new JComboBox (weightUnits);
    sexBox = new JComboBox (sexUnits);
    creatinineBox = new JComboBox (creatinineUnits);
    JButton calcButton = new JButton ("Calculate");
    calcButton.addActionListener (this);
    JPanel inputPanel = new JPanel ();
    inputPanel.setSize (width, height);
    GroupLayout layout = new GroupLayout (inputPanel);
    inputPanel.setLayout (layout);
    
    layout.setAutoCreateGaps (true);
    layout.setAutoCreateContainerGaps (true);
    
    layout.setHorizontalGroup (layout.createSequentialGroup ()
                                 .addGroup (layout.createParallelGroup()
                                              .addComponent (weightLabel)
                                              .addComponent (sexLabel)
                                              .addComponent (ageLabel)
                                              .addComponent (creatinineLabel))
                                 .addGroup (layout.createParallelGroup()
                                              .addComponent (weightField)
                                              .addComponent (sexBox)
                                              .addComponent (ageField)
                                              .addComponent (creatinineField)
                                           .addComponent (calcButton))
                                 .addGroup (layout.createParallelGroup()
                                              .addComponent (weightBox)
                                              .addComponent (creatinineBox)));
    layout.setVerticalGroup (layout.createSequentialGroup()
                                  .addGroup (layout.createParallelGroup ()
                                               .addComponent (weightLabel)
                                               .addComponent (weightField)
                                               .addComponent (weightBox))
                                  .addGroup (layout.createParallelGroup()
                                               .addComponent (sexLabel)
                                               .addComponent (sexBox))
                                  .addGroup (layout.createParallelGroup ()
                                               .addComponent (ageLabel)
                                               .addComponent (ageField))
                                  .addGroup (layout.createParallelGroup ()
                                               .addComponent (creatinineLabel)
                                               .addComponent (creatinineField)
                                               .addComponent (creatinineBox))
                                  .addGroup (layout.createParallelGroup ()
                                               .addComponent (calcButton)));
    inputPanel.setVisible (true);
    
    JLabel resultTitleLabel = new JLabel ("Results: ");
    resultsArea = new JTextArea (10,30);
    resultsArea.setEditable (false);
    JPanel resultsPanel = new JPanel ();
    SpringLayout slayout = new SpringLayout ();
    resultsPanel.setLayout (slayout);
    resultsPanel.add (resultTitleLabel);
    resultsPanel.add (resultsArea);
    
    //constraints for the title label
    slayout.putConstraint (SpringLayout.WEST, resultTitleLabel, 5, SpringLayout.WEST, resultsPanel);
    slayout.putConstraint (SpringLayout.NORTH, resultTitleLabel, 5, SpringLayout.NORTH, resultsPanel);
    
    //constraints for the textarea
    slayout.putConstraint (SpringLayout.WEST, resultsArea, 5, SpringLayout.WEST,resultsPanel);
    slayout.putConstraint (SpringLayout.NORTH,resultsArea, 5, SpringLayout.SOUTH, resultTitleLabel);
    
    //constraints for panel
    slayout.putConstraint (SpringLayout.EAST, resultsPanel, 5, SpringLayout.EAST,resultsArea);
    slayout.putConstraint (SpringLayout.SOUTH,resultsPanel, 5, SpringLayout.SOUTH,resultsArea);
    
    resultsPanel.setVisible (true);
    
    setLayout (new FlowLayout());
    add (titleLabel);
    add (inputPanel);
    add (resultsPanel);
    setVisible (true);
                                               
  }
  
  
  
  public void actionPerformed (ActionEvent ae)
  {
    String a = ae.getActionCommand();
    if (a.equals ("Calculate")){
      double[] status = checkData();
      if (hasError (status[0]) || hasError (status[1]) || hasError (status[2])){
        errorResponse (status);
      }
      else{
        calcResponse (status);
      }
    }
//    else if (e.getSource() instanceof JComboBox){
//      JComboBox cb = (JComboBox)e.getSource();
//      String unit = cb.getSelectedItem();
//      
//    }
  }
  public boolean hasError (double data){
    return data == EMPTY_DATA || data == STRING_DATA || data == BAD_DATA;
  }
  public void errorResponse (double[] data)
  {
    String errorMessage = "Error:" + "\n";
    errorMessage += errorMsg (data [0], WEIGHT);
    errorMessage += errorMsg (data [1], AGE);
    errorMessage += errorMsg (data [2], CREATININE);
    resultsArea.setText (errorMessage);
  }
  
  public String errorMsg (double data, int field)
  {
    if (data == EMPTY_DATA){
      return fieldToString (field) + " must be filled in." + "\n";
    }
    else if (data == STRING_DATA){
      return fieldToString (field) + " must be a number." + "\n";
    }
    else if (data == BAD_DATA){
      if (field == AGE){
        return "Age must be between 0 and 140." + "\n";
      }
      return fieldToString (field) + " must be a realistic value." + "\n";
    }
    return "";
  }
  
  public String fieldToString (int field)
  {
    if (field == WEIGHT)
      return "weight";
    else if (field == AGE)
      return "age";
    else if (field == CREATININE)
      return "creatinine";
    else
    return "";   
  }
  
  public static final String CREATININE_FORMULA = 
    "eCcr =  (140 - Age) x Mass (in kilograms) x Constant) /" + "\n" +
    "         Serum Creatinine (in " + ((char)181) + "mol/L)" + "\n" + 
    "\n" +
    "where Constant is 1.23 for men and 1.04 for women.";
                                                   
  public void calcResponse (double[] data)
  {
    resultsArea.setText (f.format (Calc.creatinineClearance (data[0],(int)data[1],data[2],getSex())) + " mL / min." + "\n"
                           + "\n" + CREATININE_FORMULA);
  }
  
  public boolean getSex ()
  {
    return ((String)sexBox.getSelectedItem()).equals("male")? true : false;
  }
  public static final double EMPTY_DATA = -1;
  public static final double STRING_DATA = -2;
  public static final double BAD_DATA = -3;
  public double[] checkData ()
  {
    double [] status = new double [3];
    String weightText = weightField.getText();
    String ageText = ageField.getText();
    String creatinineText = creatinineField.getText();
    status [0] = checkField (weightText, WEIGHT,(String)weightBox.getSelectedItem());
    status [1] = checkField (ageText, AGE, "");
    status [2] = checkField (creatinineText, CREATININE, (String)creatinineBox.getSelectedItem());
    return status;    
  }
  
  public static final int WEIGHT = 1;
  public static final int AGE = 2;
  public static final int CREATININE = 3;
  
  public double checkField (String text, int field, String unit)
  {
    if (text.equals ("") || text.equals (null)){
      return EMPTY_DATA;
    }
    else if (Calc.isInteger (text) == false){
      return STRING_DATA;
    }
    else{
      int data = Integer.parseInt (text);
      if (field == WEIGHT && (data >= 140 || data <= 0)){
        return BAD_DATA;
      }
      else{
        return Calc.convert (data, field, unit);
      }
    }
  }
}