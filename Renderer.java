//package BK1;

import java.util.Random;

public class Renderer {
    
    private static int _MAX_DEPTH = 3;
    private static int _MAX_SIM_RANGE = 2;
    private int r_scr_w = 1000;
    private int r_scr_h = 500;
    private int r_resolution = 25;
    private int r_bird_count;

    private int r_shadow_offset = 5;
    private double r_simu_speed = 1.0;
    private double r_frame_rate;
    private int r_frame_count=0;
    private int r_simu_interval=2;
    private double r_simu_noise = 0.1;

    // https://colorhunt.co/palette/4721834b56d282c3ecf1f6f5 directly take the RGB code
    private int[] r_bird_color = rgb(241, 246, 245);
    private int[] r_shadow_color = rgb(71, 33, 131);
    private int[] r_bg_color = rgb(75, 86, 210);

    private Bird[][][] r_birds;   // [x][y][depth]

    private double r_bird_size = 0.4f;

    private Random generator = new Random(0);

    public void init(){

        StdDraw.enableDoubleBuffering();
    }

    public void Update(){

        r_frame_count++;
        
        for(int ind_x = 0; ind_x < r_scr_w/r_resolution; ind_x++){ 
            for(int ind_y = 0; ind_y < r_scr_h/r_resolution; ind_y++){            
                for(int ind_d = 0; ind_d < _MAX_DEPTH; ind_d++){
                /*
                 * this block for interaction between bird <-> bird
                 * you need ind_x and ind_y to traverse nearest birds in r_birds[][]
                 * 
                 */
                    Bird b = r_birds[ind_x][ind_y][ind_d];
                    if(b == null || b.is_updated)continue;

                    b.is_updated = true;
/* */
                    if(r_frame_count%r_simu_interval==0){

                        for(int i=-_MAX_SIM_RANGE; i<=_MAX_SIM_RANGE; i++){
                            for(int j=-_MAX_SIM_RANGE; j<=_MAX_SIM_RANGE; j++){
                                int near_x = ind_x+i; int near_y = ind_y+j;
                                if(near_x<0 || near_x>=r_scr_w/r_resolution) near_x+=near_x<0?r_scr_w/r_resolution:-r_scr_w/r_resolution;
                                if(near_y<0 || near_y>=r_scr_h/r_resolution) near_y+=near_y<0?r_scr_h/r_resolution:-r_scr_h/r_resolution;

                                for(Bird near_b : r_birds[near_x][near_y]){
                                    if(near_b==null) continue;
                                    if(near_b==b) continue;
                                    b.InteractWith(near_b);
                                }
                            }
                        }

                        if(StdDraw.isMousePressed()){
                            b.InteractWith(new vec2(StdDraw.mouseX(), StdDraw.mouseY()));
                        }
                    }

                    
                    //b.pos = vec2.add(b.pos, vec2.vec2rotate(new vec2(b.vel*r_simu_speed, 0), b.dir));      // △x = v · △t
                    //b.dir += -generator.nextDouble()*0.1+0.05;
                    b.vel.vec2rotate(generator.nextDouble()*0.1-0.05);
                    b.pos.add(b.vel);
                    b.ReRangeDir();

                    if(!b.InRangeX(-r_scr_w/2, r_scr_w/2, 0)) b.pos.x += b.pos.x<0 ? r_scr_w : -r_scr_w;
                    if(!b.InRangeY(-r_scr_h/2, r_scr_h/2, 0)) b.pos.y += b.pos.y<0 ? r_scr_h : -r_scr_h;

                    int new_ind_x = r_scr_w/r_resolution/2 + (int)Math.floor(b.pos.x/r_resolution);
                    int new_ind_y = r_scr_h/r_resolution/2 + (int)Math.floor(b.pos.y/r_resolution);
/**/ 
                    if(new_ind_x != ind_x || new_ind_y != ind_y){
                        // Move Bird to a new location
                        for(int i = 0; i < _MAX_DEPTH; i++){
                            if(r_birds[new_ind_x][new_ind_y][i]!=null) continue;
                            r_birds[new_ind_x][new_ind_y][i] = b;
                            r_birds[ind_x][ind_y][ind_d] = null;
                            break;
                        }
                    }
                }  
            }
        }
    }
       

    public void Render(){
        int c = 0;
        StdDraw.setPenColor(r_bg_color[0], r_bg_color[1], r_bg_color[2]);
        StdDraw.filledRectangle(0, 0, r_scr_w/2, r_scr_h/2);
        for(Bird[][] __b_row : r_birds){
            for(Bird[] __b_block : __b_row){
                for(Bird b : __b_block){
                    if(b == null)continue;
                    c++;
                    //System.out.println(" ");
                    //System.out.println(b.vel.x+" "+b.vel.y);
                    //System.out.println(b.pos.x+" "+b.pos.y);
                    //RenderBird(b.pos, b.dir);
                    RenderBird(b.pos, b.vel);
                    if(!b.InRangeX(-r_scr_w/2, r_scr_w/2, 25 * r_bird_size))
                        RenderBird(vec2.add(b.pos, new vec2((b.pos.x<0?r_scr_w:-r_scr_w), 0)), b.vel);
                    if(!b.InRangeY(-r_scr_h/2, r_scr_h/2, 25 * r_bird_size))
                        RenderBird(vec2.add(b.pos, new vec2(0, (b.pos.y<0?r_scr_h:-r_scr_h))), b.vel);

                    b.is_updated = false;
                }
            }
        } 
        StdDraw.show();
        //System.out.print(c+" ");
    }

    public void RenderBird(vec2 _pos, vec2 _dir){
        vec2 ver1 = vec2.add(_pos, vec2.scale(vec2.vec2rotate(new vec2(-15,-15), vec2.dir2angle(_dir)), r_bird_size));
        vec2 ver2 = vec2.add(_pos, vec2.scale(vec2.vec2rotate(new vec2(25,0),vec2.dir2angle(_dir)), r_bird_size));
        vec2 ver3 = vec2.add(_pos, vec2.scale(vec2.vec2rotate(new vec2(-15, 15), vec2.dir2angle(_dir)), r_bird_size));
        vec2 ver4 = vec2.add(_pos, vec2.scale(vec2.vec2rotate(new vec2(-5, 0), vec2.dir2angle(_dir)), r_bird_size));

        StdDraw.setPenColor(r_shadow_color[0], r_shadow_color[1], r_shadow_color[2]);
        double[] x_s_list = {ver1.x+r_shadow_offset, ver2.x+r_shadow_offset, ver3.x+r_shadow_offset, ver4.x+r_shadow_offset};
        double[] y_s_list = {ver1.y-r_shadow_offset, ver2.y-r_shadow_offset, ver3.y-r_shadow_offset, ver4.y-r_shadow_offset};
        StdDraw.filledPolygon(x_s_list, y_s_list);                  //shadow

        StdDraw.setPenColor(r_bird_color[0], r_bird_color[1], r_bird_color[2]);
        double[] x_list = {ver1.x, ver2.x, ver3.x, ver4.x};
        double[] y_list = {ver1.y, ver2.y, ver3.y, ver4.y};
        StdDraw.filledPolygon(x_list, y_list);                  //birds
        //StdDraw.filledCircle(ver4.x, ver4.y, 10);
    }

    public void SetBird(int _count){
        r_bird_count = _count;
        
        vec2 scr_min = new vec2(-r_scr_w/2, -r_scr_h/2);
        for(int i = 0; i < r_bird_count; i++){
            Bird b = new Bird();
            b.SetRandomValue(generator.nextDouble(), scr_min, vec2.scale(scr_min, -1));
            int ind_x = r_scr_w/r_resolution/2 + (int)Math.floor(b.pos.x/r_resolution); 
            int ind_y = r_scr_h/r_resolution/2 + (int)Math.floor(b.pos.y/r_resolution); 
            
            if(r_birds[ind_x][ind_y][_MAX_DEPTH-1]!=null) break;
            for(int c = 0; c<_MAX_DEPTH; c++){
                if(r_birds[ind_x][ind_y][c]==null){r_birds[ind_x][ind_y][c] = b;break;}
            }
        }
    }

    public void SetScreen(int _w, int _h){
        r_scr_w = _w;
        r_scr_h = _h;

        StdDraw.setCanvasSize(_w, _h);

        StdDraw.setXscale(-_w/2, _w/2);
        StdDraw.setYscale(-_h/2, _h/2);

        r_birds = new Bird[_w/r_resolution][_h/r_resolution][_MAX_DEPTH];
    }

    public void Reset(){
        _MAX_DEPTH = 3;
        _MAX_SIM_RANGE = 2;
        r_scr_w = 1000;
        r_scr_h = 500;
        r_resolution = 25;
 
        r_shadow_offset = 5;
        r_simu_speed = 1.0;
        r_frame_count=0;
        r_simu_interval=2;
        r_simu_noise = 0.1;
 
        // https://colorhunt.co/palette/4721834b56d282c3ecf1f6f5 directly take the RGB code
        r_bird_color = rgb(241, 246, 245);
        r_shadow_color = rgb(71, 33, 131);
        r_bg_color = rgb(75, 86, 210);
 
 
        r_bird_size = 0.4f;
 
        generator = new Random(0);
    }

    public int[] rgb(int _r, int _g, int _b){
        int[] res = {_r, _g, _b};
        return res;
    }
}
