//package BK1;

import java.util.Random;

public class Bird {
    static int B_Behav = 1;

    public vec2 pos = new vec2();
    //public double dir;
    //public double vel;

    public vec2 vel;

    public boolean is_updated = false;

    public Bird(){
        //System.out.println("bird");
    }

    public void SetRandomValue(double seed, vec2 min, vec2 max){
        Random generator = new Random((int)(Integer.MAX_VALUE*seed));
        pos.x = generator.nextDouble()*(max.x - min.x) + min.x;
        pos.y = generator.nextDouble()*(max.y - min.y) + min.y;

        double tvel = generator.nextDouble()+0.3;
        double tdir = (double)(-2*generator.nextDouble()*Math.PI+Math.PI);
        vel = vec2.vec2rotate(new vec2(1, 0), tdir);
        vel.scale(tvel);
    }

    public void InteractWith(Bird _tar_B){
        vec2 p = vec2.subtract(_tar_B.pos, pos);
        double dist = vec2.length(p);
        double weight = Math.max(vec2.b_weight(dist, 30), -3);

        switch(B_Behav){
            case 0:
            vec2 c_dir = vec2.add(vel, _tar_B.vel);
            vel.vec2rotate(vec2.vec2angle(vel, c_dir)*0.1);
            vel.add(vec2.scale(vec2.normalize(p), weight*0.07*Math.cos(vec2.vec2angle(vel, p))));
            break;
            case 1:

            double angle = -vec2.vec2angle(p, this.vel);
            double w = Math.max(Math.cos(angle), 0);
            vel.slerp(_tar_B.vel, 0.2*w*weight);
            
            break;
        }
        vel.smooth_clamp(0.01, 2, 1);
    }

    public void InteractWith(vec2 _tar){
        vec2 del = vec2.subtract(_tar, this.pos);
        double dis = vec2.length(del);
        if(dis<=1)return;
        double factor = Math.pow(2, -dis/10+2);
        if(factor>=100) factor = 100;
        this.vel.subtract(vec2.scale(vec2.normalize(del), factor));
        //vel.smooth_clamp(0.01, 100, 1);
    }

    public boolean InRangeX(double _min, double _max, double _bound){
        return this.pos.x >=_min+_bound && this.pos.x <_max-_bound;
    }

    public boolean InRangeY(double _min, double _max, double _bound){
        return this.pos.y >=_min+_bound && this.pos.y <_max-_bound;
    }

    public boolean InRange(vec2 _min, vec2 _max, double _bound){
        return InRangeX(_min.x, _max.x ,_bound) && InRangeX(_min.y, _max.y, _bound);
    }

    public void ReRangeDir(){
        //if(this.dir<-Math.PI) this.dir+=2*Math.PI;
        //if(this.dir>=Math.PI) this.dir-=2*Math.PI;
    }
}
