import java.util.Scanner;
class grade {
    String Student_name = "";
    double Mark = 0;
    void setMark(String Student_name,int Mark) {
        this.Student_name = Student_name;
        this.Mark = Mark;
    }
    void display(){
        System.out.println(Student_name +"       "+ Mark );
    }
    public static void main(String[] args){
      Scanner v = new Scanner(System.in);
        System.out.print("Number of Students : ");
      int k = v.nextInt();
      grade[] s1 = new grade[k];
      double j=0;

      for(int i=0;i<k;i++){
          v.nextLine();
          s1[i] = new grade();
          System.out.print("Enter Student "+(i+1)+" Name : ");
          String m = v.nextLine().toUpperCase();
          System.out.print("Enter Student "+(i+1)+" Mark : ");
          int n = v.nextInt();

          s1[i].setMark(m,n);
      }
        System.out.println("Student Name             Score");
        System.out.println("-------------------------------");
      for(int i=0;i<k;i++){
          s1[i].display();
      }
        for(int i=0;i<k;i++){
            j = j + s1[i].Mark;
        }
        double h = s1[0].Mark;
        for(int i=0;i<k;i++){
            for(int q=i+1;q<k;q++){
                if(s1[i].Mark<s1[q].Mark){
                    h=s1[q].Mark;
                }
            }
        }
        double z =s1[0].Mark;
        for(int i=0;i<k;i++){
            for(int q=i+1;q<k;q++){
                if(s1[i].Mark>s1[q].Mark){
                    z=s1[q].Mark;
                }
            }
        }
        System.out.println("----------------------------------------------------");
        System.out.println("Average Mark  : "+j/k);
        System.out.println("Highest Mark  : "+h);
        System.out.println("Lowest  Mark  : "+z);

    }
}
