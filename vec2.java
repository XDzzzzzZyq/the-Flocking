//package BK1;

import java.lang.Character.Subset;

public class vec2{
    public double x = 0; 
    public double y = 0;

    public vec2(double _x, double _y){
        this.x = _x;
        this.y = _y;
    }

    public vec2(){
        this.x = 0;
        this.y = 0;
    }

    public vec2(double vec[]){
        this.x = vec[0];
        this.y = vec[1];
    }

    public vec2(vec2 _tar){
        this.x = _tar.x;
        this.y = _tar.y;
    }

    public boolean is_inRange(vec2 _min, vec2 _max){
        if(this.x<_min.x || _max.x<this.x) return false;
        if(this.y<_min.y || _max.y<this.y) return false;
        return true;
    }


    public static vec2 add(vec2 _a, vec2 _b){
        return new vec2(_a.x + _b.x, _a.y + _b.y);
    }

    public void add(vec2 _b){
        this.x+=_b.x;
        this.y+=_b.y;
    }

    public static vec2 subtract(vec2 _a, vec2 _b){
        return new vec2(_a.x - _b.x, _a.y - _b.y);
    }

    public void subtract(vec2 _b){
        this.x-=_b.x;
        this.y-=_b.y;
    }

    public static vec2 scale(vec2 _a, double _s){
        return new vec2(_a.x * _s, _a.y * _s);
    }

    public void scale(double _s){
        this.x*=_s;
        this.y*=_s;
    }

    public static double length(vec2 _a){
        return (double)Math.sqrt(_a.x*_a.x+_a.y*_a.y);
    }

    public static double dist(vec2 _a, vec2 _b){
        return length(subtract(_a, _b));
    }

    public static vec2 normalize(vec2 _a){
        return scale(_a, 1/length(_a));
    }

    public void normalize(){
        double l = length(this);
        this.x/=l;
        this.y/=l;
    }

    public static vec2 vec2rotate(vec2 _a, double angle){
        double x = Math.cos(angle)*_a.x - Math.sin(angle)*_a.y;
        double y = Math.sin(angle)*_a.x + Math.cos(angle)*_a.y;
        return new vec2(x, y);
    }

    public static vec2 slerp(vec2 _a, vec2 _b, double _p){
        return new vec2(_a.x+(_b.x-_a.x)*_p, _a.y+(_b.y-_a.y)*_p);
    }

    public void slerp(vec2 _b, double _p){
        this.x += (_b.x-this.x)*_p;
        this.y += (_b.y-this.y)*_p;
    }

    public void slerp_dir(vec2 _b, double _p){
        double speed = vec2.length(this);
        double speed_b = vec2.length(_b);
        this.x += (_b.x-this.x)*_p;
        this.y += (_b.y-this.y)*_p;
        normalize();
        scale(speed*(1-_p)+speed_b*(_p));
    }

    public void slerp_mag(vec2 _b, double _p){
        double speed = vec2.length(this);
        double speed_b = vec2.length(_b);
        normalize();
        scale(speed*(1-_p)+speed_b*(_p));
    }

    public void clamp(double min, double max){
        double speed = length(this);
        if(speed<min){
            normalize();
            scale(min);
        }
        else if(speed>min){
            normalize();
            scale(max);
        }
    }

    public void smooth_clamp(double min, double max, double t){
        double speed = length(this);
        if(speed<min){
            normalize();
            scale(min);
        }
        else if(speed>min){
            normalize();
            scale(speed-Math.pow(2, (speed-max))*t+t);
        }
    }

    public static double dot(vec2 _a, vec2 _b){
        return _a.x*_b.x + _a.y+_b.y;
    }
    
    public void vec2rotate(double angle){
        this.x = Math.cos(angle)*this.x - Math.sin(angle)*this.y;
        this.y = Math.sin(angle)*this.x + Math.cos(angle)*this.y;
    }

    public static double dir2angle(vec2 _a){
        vec2 temp = normalize(_a);
        return Math.atan2(temp.y, temp.x);
    }

    public static double b_weight(double _dis, double _balance){
        return Math.pow(_dis/_balance, -1.5) - Math.pow(_dis/_balance, -4.5);
    }

    public static double vec2angle(vec2 _a, vec2 _b){
        return Math.asin((_a.x*_b.y - _a.y*_b.x)/length(_a)/length(_b));
    }
}
