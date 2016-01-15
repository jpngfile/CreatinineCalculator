public class Calc
{
 public Calc()
 {
 }
 
 /*
  * Assume that the data is not negative and is valid
  */
 public static double convert (int data, int field,String unit)
 {
   if (field == MainPanel.WEIGHT){
     if (unit.equals ("lb")){
       return data*0.453592;
     }
     return (double)data;
   }
   //multiply by 88.4?
   else if (field == MainPanel.CREATININE){
     if (unit.equals ("mg/dL")){
       return data*88.4;
     }
     return (double)data;
   }
   else{
     return (double)data;
   }
 }
 
 public static boolean isInteger (String text)
 {
   try {
     Integer.parseInt (text);
     return true;
   }
   catch (NumberFormatException e){
   }
   return false;
 }
 public static final boolean MALE = true;
 public static final boolean FEMALE = false;
 
 public static double creatinineClearance (double weight,int age,double creatinine,boolean sex)
 {
   return ((140 - age)*weight*(sex == MALE? 1.23 : 1.04)) / creatinine;
 }
}