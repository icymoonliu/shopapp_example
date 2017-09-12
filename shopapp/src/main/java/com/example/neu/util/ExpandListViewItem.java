package com.example.neu.util;


import com.example.neu.shopapp.R;

public class ExpandListViewItem {
	
		 private String id;
	     private int imgId1; 
	     private String name; 
	     private int imgId2; 
	     
	     public ExpandListViewItem(String id,String name) { 
	    	 this.id = id;
	         this.imgId1  = R.drawable.book;
	         this.name   = name; 
	         this.imgId2 = R.drawable.rightarrow;
	          
	     } 
	      
	     public ExpandListViewItem(String id,int imgId1, String name, int imgId2) { 
	    	 this.id = id;
	         this.imgId1  = imgId1; 
	         this.name   = name; 
	         this.imgId2 = imgId2;
	          
	     } 
	     
	     

	     public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getImgId1() {
			return imgId1;
		}



		public void setImgId1(int imgId1) {
			this.imgId1 = imgId1;
		}



		public int getImgId2() {
			return imgId2;
		}



		public void setImgId2(int imgId2) {
			this.imgId2 = imgId2;
		}



		public void setName(String name) { 
	         this.name   = name; 
	     } 
	      
	     public String getName() { 
	         return name; 
	     } 
	      
	   
	      
	     public String toString() { 
	         return "Item["+ id+ "," + imgId1 + ", " + name + "," + imgId2 + "]"; 
	     } 
	  
	
}
