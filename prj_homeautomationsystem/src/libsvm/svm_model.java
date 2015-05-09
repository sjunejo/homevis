//
// svm_model
//
package libsvm;


/**
 * LibSVM Java Implementation
 * @author Chih-Chung Chang and Chih-Jen Lin from National Taiwan University
 * Copyright (c) 2000-2014 Chih-Chung Chang and Chih-Jen Lin
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

3. Neither name of copyright holders nor the names of its contributors
may be used to endorse or promote products derived from this software
without specific prior written permission.


THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class svm_model implements java.io.Serializable
{
	
	svm_parameter param;	// parameter
	int nr_class;		// number of classes, = 2 in regression/one class svm
	int l;			// total #SV
	svm_node[][] SV;	// SVs (SV[l])
	double[][] sv_coef;	// coefficients for SVs in decision functions (sv_coef[k-1][l])
	double[] rho;		// constants in decision functions (rho[k*(k-1)/2])
	double[] probA;         // pariwise probability information
	double[] probB;
	int[] sv_indices;       // sv_indices[0,...,nSV-1] are values in [1,...,num_traning_data] to indicate SVs in the training set

	// for classification only

	int[] label;		// label of each class (label[k])
	int[] nSV;		// number of SVs for each class (nSV[k])
				// nSV[0] + nSV[1] + ... + nSV[k-1] = l
	
	
	public svm_parameter getParam() {
		return param;
	}
	public void setParam(svm_parameter param) {
		this.param = param;
	}
	public int getNr_class() {
		return nr_class;
	}
	public void setNr_class(int nr_class) {
		this.nr_class = nr_class;
	}
	public int getL() {
		return l;
	}
	public void setL(int l) {
		this.l = l;
	}
	public svm_node[][] getSV() {
		return SV;
	}
	public void setSV(svm_node[][] sV) {
		SV = sV;
	}
	public double[][] getSv_coef() {
		return sv_coef;
	}
	public void setSv_coef(double[][] sv_coef) {
		this.sv_coef = sv_coef;
	}
	public double[] getRho() {
		return rho;
	}
	public void setRho(double[] rho) {
		this.rho = rho;
	}
	public double[] getProbA() {
		return probA;
	}
	public void setProbA(double[] probA) {
		this.probA = probA;
	}
	public double[] getProbB() {
		return probB;
	}
	public void setProbB(double[] probB) {
		this.probB = probB;
	}
	public int[] getSv_indices() {
		return sv_indices;
	}
	public void setSv_indices(int[] sv_indices) {
		this.sv_indices = sv_indices;
	}
	public int[] getLabel() {
		return label;
	}
	public void setLabel(int[] label) {
		this.label = label;
	}
	public int[] getnSV() {
		return nSV;
	}
	public void setnSV(int[] nSV) {
		this.nSV = nSV;
	}
	
	
	
	
	
};
