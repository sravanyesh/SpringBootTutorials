package com.springboot.learn.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.learn.model.Product;

@Component
public class ServiceLogic {
	public List<Product> prd=new ArrayList<> (Arrays.asList(new Product(11,"TV",30000),new Product(12,"AC",40000)));
	public List<Product> getproducts(){
		return prd;
	}
	public Product getproductbyid(int i) {
		Product p=null;
		for (Product product : prd) {
			if(product.getPid()==i)
			{
				p=product;
				break;
			}
			
		}
		return p;
		
	}
	public void addnew(Product p) {
		// TODO Auto-generated method stub
		prd.add(p);
	}
	public void updateproduct(Product p) {
		// TODO Auto-generated method stub
		int index=0;
		for(int i=0;i<prd.size();i++) {
			if(prd.get(i).getPid()==p.getPid()) {
				index=i;
			}
		
			prd.set(index, p);
			
			
		}
		
	}
	public void delete(int pid) {
		// TODO Auto-generated method stub
		for(int i=0;i<prd.size();i++) {
			if(prd.get(i).getPid()==pid) {
				prd.remove(i);
			}
				
			}
		
	}

}
