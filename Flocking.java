//package BK1;

public class Flocking {

    public static Renderer renderer = new Renderer();

    public static void init(int count){
        renderer.SetScreen(1000, 500);
        renderer.SetBird(count);
        renderer.init();
    }
    public static void main(String[] args) {
        while(true){
            init(40);
            //while(true){
            //    if(StdDraw.isKeyPressed(33)) break;  //enter
            //}
            while(true){
                if(StdDraw.isKeyPressed(32)) break;  //space bar
                if(StdDraw.isKeyPressed(27)) break;  //esc
                if(StdDraw.isKeyPressed(49)) Bird.B_Behav = 0;  //1
                if(StdDraw.isKeyPressed(50)) Bird.B_Behav = 1;  //2
                if(StdDraw.isKeyPressed(51)) Bird.B_Behav = 2;  //3
                if(StdDraw.isKeyPressed(52)) Bird.B_Behav = 3;  //4
                renderer.Update();
                renderer.Render();
            }
            StdDraw.pause(100);
            terminate();
            if(StdDraw.isKeyPressed(27)) break;  //esc
        }
        System.exit(0);
    }

    public static void terminate(){
        renderer.Reset();
    }
}
