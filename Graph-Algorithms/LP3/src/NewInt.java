/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aditya
 */
public class NewInt implements Index {

	int index;
	Integer val;

	NewInt(int val) {
		this.val = val;
	}

	public void putIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

}
