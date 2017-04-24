package com.github.wesleyegberto.mathlib.impl;

import com.github.wesleyegberto.mathapi.Operation;
import com.github.wesleyegberto.mathlib.*;
/**
 * This implementation we won't export, but we can access it
 * through its interface - Operation
 */
public class SubtractionOperation implements Operation<Integer, Integer> {
	public Integer apply(Integer x, Integer y) {
		return x - y;
	}
}