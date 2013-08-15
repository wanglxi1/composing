package com.teddytailor.research.compostion.aima.data;

import java.util.Comparator;


public class OrderInteger{
	public final static Comparator<OrderInteger> COMPARATOR = new Comparator<OrderInteger>() {
		@Override
		public int compare(OrderInteger o1, OrderInteger o2) {
			return Integer.valueOf(o1.order).compareTo(o2.order);
		}
	};
	
	public int origin;
	public boolean reversal;
	
	public int order;
	
	public final static OrderInteger valueOf(int origin, int order) {
		OrderInteger oi = new OrderInteger();
		oi.order = Math.abs(order);
		
		oi.reversal = order<0;
		oi.origin = origin;
		return oi;
	}
	
	public int toInt() {
		return this.order * (this.reversal? -1: 1);
	}
	
	public byte toByte() {
		return (byte)(this.origin* (this.reversal? -1: 1));
	}
	
	@Override
	public int hashCode() {
		return origin*2 + (reversal? 1: 0);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof OrderInteger) {
			OrderInteger oi = (OrderInteger)obj;
			return this.origin==oi.origin && this.reversal==oi.reversal;
		}
		return false;
	}

	@Override
	public String toString() {
		return (reversal?'T':'F')+""+origin;
	}
	
	
}