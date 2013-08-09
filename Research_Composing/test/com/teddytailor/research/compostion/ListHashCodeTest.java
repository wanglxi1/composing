package com.teddytailor.research.compostion;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.teddytailor.research.compostion.aima.data.ComposingModel;
import com.teddytailor.research.compostion.aima.data.ModelFactory;

public class ListHashCodeTest {
	
	@Test
	public void test() throws Exception{
		File parent = new File(ModelFactory.RESOURCE, "dat");
		
		ComposingModel o2 = new ComposingModel(ModelFactory.readData(new File(parent, "内胳_右_1.dat")));
		ComposingModel o3 = new ComposingModel(ModelFactory.readData(new File(parent, "手掌_右_1.dat")));
		ComposingModel o4 = new ComposingModel(ModelFactory.readData(new File(parent, "手掌_左_1.dat")));
		
		Map m = new HashMap();
		
		List<ComposingModel> ls = new ArrayList<ComposingModel>();
		o2.reversal = true;
		ls.add(o2);
		ls.add(o3);
		ls.add(o4);
		System.out.println(ls.hashCode());
		m.put(ls, 1);
		
		ls = new ArrayList<ComposingModel>();
		ls.add(o2);
		ls.add(o4);
		ls.add(o3);
		System.out.println(ls.hashCode());
		m.put(ls, 2);
		
		ls = new ArrayList<ComposingModel>();
		o3.reversal = true;
		ls.add(o2);
		ls.add(o3);
		ls.add(o4);
		System.out.println(ls.hashCode());
		
		m.put(ls, 3);
		
		System.out.println(m);
		
	}
}
