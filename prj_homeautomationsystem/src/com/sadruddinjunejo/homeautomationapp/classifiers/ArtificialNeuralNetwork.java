package com.sadruddinjunejo.homeautomationapp.classifiers;

import java.util.Arrays;

import android.util.Log;

import com.sadruddinjunejo.homeautomationapp.utils.MatrixOperations;
import com.sadruddinjunejo.homeautomationapp.utils.SingleArrayOperations;


/**
 * The Neural Network function created in MATLAB
 * (using the Neural Network Toolbox) translated
 * into Java code for use  within the app.
 * @author Sadruddin Junejo
 *
 */
public class ArtificialNeuralNetwork implements IClassifier {

	/*
	 * The following paramaters have been taken from
	 * the ANN implementation built with
	 * MATLAB's Neural Network Toolbox
	 */
	// Input 1

	double[][] x1_step_offset = new double[][]{{-16.6751164793247,-16.2575377738276,-16.6588867360932,-14.4585711462428,-14.3833824886157,-11.3440168498587,-14.7217503880885,-12.2620861174445,-10.7456998472526,-8.06897887598796,-7.01171318543921,-7.78585876562619,-6.96431562864409,-7.94489109313385,-7.00595585761753,-6.23683931153507,-8.38517301526058,-8.49663328019165,-5.06805816919813,-6.63828494229132,-6.55480605665116,-7.29521763250452,-6.872200997261,-5.39827442316862,-7.10082895511169,-6.57753093923839}};
	
	
	double[][] x1_step1_gain = new double[][]{{0.0604540523711461,0.059246593900924,0.0592244025867545,0.0665198158828174,0.0693657741309004,0.0829603239229926,0.0809650003631897,0.0867174308318301,0.0973425774139669,0.108349276444577,0.0986716669082447,0.109167980342569,0.107115196244124,0.134199081901776,0.134871751358612,0.136803471276603,0.112435908680985,0.118542508306806,0.178106681685576,0.12625798184718,0.147002529297464,0.154568849173882,0.12901202905418,0.157847068662624,0.126705245723813,0.137682628441471}};

	
	double x1_step1_ymin = -1;
	
	double[][] b1 = new double[][]{{1.0124954116381179592,-0.88417749519431443339,-0.26493163759589349882,0.2264474027961927427,-0.059642736942732157979,0.27229308956153858157,-0.13789764726346717683,0.28430068350705228752,-0.79678315045892444957,0.85103793272980199358}};


	double[][] IW1_1 ={{-0.49125476537920848008,-0.77982410135580804145,-0.10301657850192263988,0.32320976339501406027,-0.76362625897691094323,-0.040841397907569423897,0.22180557615942536609,-0.68057851896859777874,0.016543236863047885987,-0.3942126138557231485,0.30574998094843353069,0.72934732630217058436,0.42702444871748479471,-0.59994673178908652922,-0.21452866805490927726,-0.060524421749141563143,-0.12349606276734238774,0.22974312551255240966,0.016912605591669332278,-0.19510311523264439737,0.55995084242052350909,-0.27952613944584209227,-0.21102078446817376656,0.085865348700989954644,0.43551190192782668342,0.24282269909600878721},{0.91907744586649775531,-0.66680679355237615358,0.075784391210724791343,0.35981814755103558312,0.026154466404913589983,-0.28522466682113462033,0.21608637789842352617,0.21286015075235437122,-0.45519453735544662676,-0.38779226384175363718,-0.56165511537689172261,-0.16945536719571890893,-0.17660949582167720373,0.59930428190722961901,-0.27841047907295884967,0.53273167516344888117,0.41881287760481400451,-0.11974809886069830156,0.22950484018144573506,-0.013365123986948258816,0.03372438806463630423,0.18175349675687349493,0.15875327701081959342,0.0002832275073194681636,0.42355124753487516376,-0.071871004239239755229},{2.0661303324517188429,-0.893399776869483353,0.017141725907165950016,-0.49185758011490576802,0.86722289735883317796,-0.38478054954819046474,0.14969638714416147462,-0.18491092080292106936,0.62048216641146036743,0.91892785068693483286,0.042032358537393871289,0.2845315849969973776,0.21303354648206052069,-0.075435456427130356816,0.072007398682747622565,-0.5491585482141702057,0.039798796697448374227,0.17399032212972403988,-0.091224048810402871834,-0.070892345718347377082,0.064324029312767885824,-0.1082460816922736907,-0.42324237225675243046,0.31488451202433775933,0.43907456399310118567,0.17216379418889382613},{0.41124883555768759757,-0.20385214880790333836,-1.0641532684301762135,-0.35635256941522763796,0.041233361456113420962,-1.0689436624369015316,0.28579154709906118281,0.66880724633336674501,-1.1177682404006696881,-0.70003675630546291497,0.60028261183223019515,0.1931197608513132713,0.17354880178631565402,-0.074425991004819919139,-0.29704551046633392852,-0.033559427852131923364,0.062577235141532794316,-0.29963749704075126612,-0.48085592793200482298,0.31368100994372333234,0.35920683363482841521,0.037004354402355388309,-0.095954669756614596743,-0.2357386989071222072,-0.28681808239388334458,0.15092709067222565},{0.42337575461920645692,0.4093422722242133216,-0.77970178109864596294,0.30574066821472828392,-1.3516810579313895957,-0.56882233528739467587,-0.69360567952295337779,-0.66678623301418049607,-0.89318770586354301333,-0.053999106775399131897,0.24986797438794849935,0.41391501911135353264,-0.22821565314897734877,-0.25258765048468262426,-0.47935366880684571189,-0.032633441636851910628,-0.063482485611142461246,-0.38121122091368181417,0.18094181923309110571,-0.47853489891866651806,0.29824036011986904127,-0.075231025541972862403,-0.051055048676370028138,-0.22259768743402974511,-0.22169040880727633458,0.56089782864984638611},{0.1107507586224126539,1.5325568961401854207,0.17530797890569593811,0.11974951189304389176,1.1050031024884114927,-1.5609334496696427586,0.017580919721069371287,-0.15256319999120515618,-0.31173815549281574944,-0.24155945722734264991,0.067413890310419138929,0.3099238383848621492,-0.79263809617998537416,0.11949809469365781089,0.39014718154710220643,0.69140169933106965239,0.030920936371015658728,-0.38499640744198310172,0.038256476080829215913,-0.039258842968463103507,-0.061061151641685081082,0.060264082535246579764,-0.060144831264987805297,-0.0050421433576139304544,-0.091774492984346658919,0.14630592816600634931},{1.1559433395399887345,-1.0203782002860586342,-0.2501348505178445647,-0.29479191926294562309,0.12876695049319547559,-0.46815539737130634768,0.44989494936939861125,-0.068079470492316773722,0.43663831133029568088,0.85968630580674909591,-0.23035218890074579678,0.26473944150838851774,-0.092577572089799317623,0.14535532951515950373,0.62496821902177346342,0.051880675283219671035,0.15816019208068546331,0.1535375438931935177,-0.13233906630104541602,-0.32560073988780480203,-0.15597968394968886319,-0.31854520539570496673,0.017579685224974564428,0.16681554197086048696,0.26577553262365777309,-0.20217637636283775082},{0.89074706864738029122,1.8471121249146082999,0.65934626968139953274,-0.095475182920741225034,-0.058327737744054114488,0.0018619514496001979276,-0.33184198759346983376,-1.1157316471641913758,0.28110461978657069748,-0.087829043738060885715,-0.62236107993171108088,0.10474177803847445345,-0.51531809919765914074,-0.51036198038908109353,0.038232241487504349531,0.31027732764342708105,0.37955207814179836578,0.22288750543925683889,0.13422660013230844345,0.049117882199236670426,-0.11429726435060358347,-0.186145402999108589,-0.046922850069139820928,0.004849004880231287673,-0.26875967412877488361,0.36948478826998187863},{-1.0355993724449690241,-0.7757062498360727254,0.3626752082503262864,-0.33790979636002210773,0.95845513233356982585,-0.9613541553320016364,0.27413996018156794321,0.20734099884699652705,0.26399867261608411795,-0.38827535541387325768,0.52406320435682152148,-0.53388052968544241761,-0.35176624871326600541,0.055947940620376168663,-0.18792571492367260433,0.034212136158994134461,-0.54053815684514661122,0.19649088643897996698,0.00066209926952926802246,0.041093462148490925157,0.15391794052555052286,0.16509711722494113673,-0.053516528561181241075,0.55830735696622879605,0.14706485034514291121,-0.50119346908621797976},{0.71874771716306917746,-0.46338291070224457702,-0.67032118497300174909,0.21691184160997586949,0.40194587010072257982,-0.64123845831022274755,-0.59649493684517851388,-0.27811988669043291589,-0.37778929252920190329,-0.38632813206817673279,0.35740318404753745885,0.59059226274639442522,0.22573365809290682482,0.14746598191245180454,-0.15631237474701520362,0.1973541638235416229,0.44978271283697368599,0.14509299489459659416,-0.31676910645445938153,0.015243128793388609904,0.14758638280018554489,-0.50671561764043615561,0.43587941156372633777,-0.27206733341276551075,-0.28458929019171635977,-0.37764664999964042691}};

	// Layer 2
	double[][] b2 = new double[][]{{-0.49284378127519706814,-0.46077512388787633668,0.61368363696516736727,-0.56502953069937078556,-0.68586165088829842418}};

	double[][] LW2_1 = new double[][]{{0.46219441642041364515,0.71722261879500281978,-1.6253929247559171234,1.0776763733655569943,1.4404162689199306868,1.3625757111687351042,-0.3936365236084553576,0.39366102033985844733,-0.67538146711844793124,-0.043868294620284950525},{-0.23466361256318610251,0.33798605742942794405,1.9008954797535801973,0.30627249502143949478,-0.86977028351891094715,1.7672195482394017496,1.2184936307046461135,1.5513882045995091019,0.27460646091561841109,0.4561342215269011402},{-0.21893052070557811328,0.28264852554650160554,-0.86529288117064362407,0.80957459360332828524,-1.3549272418176012689,0.43136159921315286869,0.32331185367833886746,-2.4290691639829287141,1.7415808287469980442,0.37602276367594089646},{1.4422330012805302246,0.69978530366952074182,0.83107346172326312939,0.36172949918578928274,-0.018589680824107491369,-2.1458594873855210139,1.3417120396490558321,-1.2182911662952566889,-1.1685666434834816041,0.41201531914637767873},{0.026382162755109218877,-0.9531582958525504079,-1.6153898749434727478,-2.1101449859831253875,-1.3686723137681151563,-0.080085503851713646384,-0.70050245229143093262,-0.0022194251200041148486,-0.20141740596325671819,-1.1593187525997152854}};

	public ArtificialNeuralNetwork(){
		Log.d("IW_LENGTH", "" + IW1_1.length + " x " + IW1_1[0].length);
	}
	
	
	/**
	 * Called after speech is recorded
	 * Takes in reduced feature vector
	 * and returns an integer corresponding
	 * to one of the five appliances
	 */
	@Override
	public int classify(double[][] features) {
		double[][] x = mapMiniMaxApply(features);
		
		 x = MatrixOperations.multiplyMatrices(IW1_1,
				MatrixOperations.transposeMatrix(x));
		
		double[][] a1 = MatrixOperations.addMatrices(MatrixOperations.transposeMatrix(b1), x);
		
		for (int i = 0; i < a1.length; i++){
			for (int j = 0; j < a1[0].length; j++){
				a1[i][j] = tansigApply(a1[i][j]);
			}
		}
		
		double[][] a2 = MatrixOperations.addMatrices(MatrixOperations.transposeMatrix(b2),
				MatrixOperations.multiplyMatrices(LW2_1, a1));
		
		a2 = softmaxApply(a2);
		
		MatrixOperations.printMatrix(a2, "Final result!");
		
		// Get probabilities of each appliance
		double[] result = new double[5];
		for (int i = 0; i < result.length; i++){
			result[i] = a2[i][0];
		}
		
		return SingleArrayOperations.indexWithMaxValue(result);
	}

	/**
	 * Sigmoid-transfer function
	 */
	private double tansigApply(double n){
		double a = 2 / (1 + Math.exp(-2 * n)) - 1;
		return a;
	}
	
	
	/**
	 * Applies softmax normalisation function on 2D matrix
	 * @param n 
	 * @return output of the function
	 */
	private double[][] softmaxApply(double[][] n){
		double[] maxValues = nmax(n);
		System.out.println(Arrays.toString(maxValues));
		
		// n = bsxfun(@minus,n,nmax)
		double[][] numer = new double[n.length][n[0].length];
		// Subtract max from each column in the 2-D Matrix
		for (int i = 0; i < n.length; i++){
			for (int j = 0; j < n[0].length; j++){
				n[i][j] = n[i][j] - maxValues[0]; // there's only gonna be one maxValue lol
				
				// Exponential on each element
				numer[i][j] = Math.exp(n[i][j]);
			}
		}
		
		// So far, so good...
		MatrixOperations.printMatrix(n, "n: ");
		MatrixOperations.printMatrix(numer, "number: ");
		
		double[] denom = new double[n.length];

		double sum = 0;
		for (int i = 0; i < numer.length; i++){
			//for (int j = 0; j < numer[0].length; j++){
				sum += numer[i][0];
			//}
		}
		if (sum == 0) sum = 1;
		denom[0] = sum;
		Log.d("DENOM", Arrays.toString(denom));
		
		
		// bsxfun(@rdivide,numer,denom);
		for (int i = 0; i < numer.length; i++){
			for (int j = 0; j < numer[0].length; j++){
				numer[i][j] = numer[i][j] / denom[0]; // This likely will not work but ah well
			}
		}
		
		return numer;
	}
	
	private double[] nmax(double[][] n){
		double[] maxValues = new double[n.length];
		
		n = MatrixOperations.transposeMatrix(n);
		
		// Find max of each column
		for (int i = 0; i < n.length; i++){
			double max = Double.MIN_VALUE;
			for (int j = 0; j < n[0].length; j++){
				if (n[i][j] > max){
					max = n[i][j];
				}
			}
			maxValues[i] = max;
		}
		return maxValues;
	}
	
	private double[][] mapMiniMaxApply(double[][] x){
		for (int i = 0; i < x.length; i++){
			for (int j = 0; j < x[0].length; j++){
				x[i][j] = x[i][j] - x1_step_offset[i][j]; 
				x[i][j] = x[i][j] * x1_step1_gain[i][j];
				x[i][j] = x[i][j] + x1_step1_ymin;
			}
		}
		return x;
	}
}
