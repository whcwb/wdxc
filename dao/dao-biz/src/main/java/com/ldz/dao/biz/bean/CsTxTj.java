package com.ldz.dao.biz.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsTxTj {

	List<String> list = new ArrayList<String>(){
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
            add("8点-10点");
            add("10点-12点");
            add("12点-14点");
            add("14点-16点");
            add("16点-18点");
            add("18点-20点");
            add("20点-22点");
        }
    };
	
	private List<Integer> count;
}
