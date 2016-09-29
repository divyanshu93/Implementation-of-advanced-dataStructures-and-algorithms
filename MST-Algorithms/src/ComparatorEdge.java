/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aditya
 */
import java.util.Comparator;

public class ComparatorEdge implements Comparator<Edge> {

	@Override
	public int compare(Edge arg0, Edge arg1) {

		return arg0.Weight - arg1.Weight;
	}

}
