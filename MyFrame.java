import javax.swing.JFrame;

public class MyFrame extends JFrame{
    
    private MyGameScreen mgs;

    public MyFrame(){
        super();
    }

    public MyFrame(int width, int height){
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("MyFrame");
        setVisible(true);
    }

    public void setGameScreen(MyGameScreen mgs){
        this.getContentPane().add(mgs);
        this.mgs = mgs;
    }

    public MyGameScreen getGameScreen() throws NullPointerException{
        if(mgs == null){
            throw new NullPointerException("No Game Screen added to Frame!");
        }
        return mgs;
    }

}
