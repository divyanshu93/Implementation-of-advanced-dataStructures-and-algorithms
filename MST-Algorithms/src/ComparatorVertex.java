/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Comparator;

/**
 *
 * @author aditya
 */
public class ComparatorVertex implements Comparator<Vertex> {

	@Override
	public int compare(Vertex arg0, Vertex arg1) {

		return arg0.d - arg1.d;
	}
}
