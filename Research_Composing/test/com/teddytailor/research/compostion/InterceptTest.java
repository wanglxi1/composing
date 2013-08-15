package com.teddytailor.research.compostion;

import java.awt.Rectangle;

import org.junit.Test;

public class InterceptTest {
	
	@Test
	public void test() {
		int w=131, h=191;
		int x=-69, y=-33;
		
		Rectangle r1 = new Rectangle(-x, -y, w, h);
		Rectangle r2 = new Rectangle(0, 0, w, h);
		
		Rectangle ir = intersection(r1, r2);
		System.out.println(ir);
		
		ir = intersection(r2, r1);
		System.out.println(ir);
	}
	
	public static Rectangle intersection(Rectangle t, Rectangle r) {
        int tx1 = t.x;
        int ty1 = t.y;
        int rx1 = r.x;
        int ry1 = r.y;
        long tx2 = tx1; tx2 += t.width;
        long ty2 = ty1; ty2 += t.height;
        long rx2 = rx1; rx2 += r.width;
        long ry2 = ry1; ry2 += r.height;
        if (tx1 < rx1) tx1 = rx1;
        if (ty1 < ry1) ty1 = ry1;
        if (tx2 > rx2) tx2 = rx2;
        if (ty2 > ry2) ty2 = ry2;
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never overflow (they will never be
        // larger than the smallest of the two source w,h)
        // they might underflow, though...
        if (tx2 < Integer.MIN_VALUE) tx2 = Integer.MIN_VALUE;
        if (ty2 < Integer.MIN_VALUE) ty2 = Integer.MIN_VALUE;
        return new Rectangle(tx1, ty1, (int) tx2, (int) ty2);
    }
}
