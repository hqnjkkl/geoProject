package com.geoImage.dataStatistics;

/**
 * 用来统计我们的方法在准确率，召回率，还有方差方面的一些数据。
 * 
 * @author hqn
 * @description
 * @version
 * @update 2014-12-24 下午4:50:21
 */

public class toponymyCalculate {
	/**
	 * @param args
	 */
	// ground是每篇文章真正的地名词个数
	double ground[] = { 7, 10, 7, 0, 22, 3, 10, 9, 4, 8 };
	// 第一维度是wwj，第二维度是yahoo place maker,第三维度是ours
	double tableTotal[][] = { { 11, 15, 9, 0, 34, 3, 33, 14, 12, 6 },
			{ 11, 6, 4, 0, 11, 2, 15, 10, 1, 4 },
			{ 12, 9, 7, 0, 31, 2, 18, 11, 4, 6 } };
	double tableCorrect[][] = { { 5, 5, 4, 0, 14, 1, 7, 4, 2, 3 },
			{ 7, 3, 3, 0, 6, 2, 9, 5, 1, 4 },
			{ 7, 5, 4, 0, 18, 2, 9, 5, 2, 5 } };
	//之前计算得到的avg，同样第一行是wwj，第二行是yahoo placemaker,第三行是自己
	//第一列是输出个数的平均数，第二列是准确率的平均数,第三列是召回率的平均数
	double avg[][] = {{13.7,0.414,0.577},
			{6.4,0.75,0.58},
			{10,0.66,0.71}
			};
	double avgGround = 10;
	public static void main(String[] args) {
		toponymyCalculate tCalculate = new toponymyCalculate();
		tCalculate.getCalculate();
	}
	public void getVar()
	{
		double varTotal=0,varPre1=0,varRecall1=0;
		for(int j=0;j<3;j++)
		{
			for(int i=0;i<10;i++)
			{
				
			}
		}
	}
	// 需要生成precision, recall,precision 方差, recall 方差
	// 平均个数average,生成了avg之后，再通过avg算出标准差，

	public void getCalculate() {
		double avgGround=0,avg1Total = 0, avg1Precision = 0,avg1Recall=0;
		double varTotal=0,varPre1=0,varRecall1=0;
		
		int j = 0;
		for(j=0;j<3;j++)
		{
		for (int i = 0; i < 10; i++) {
			avgGround += ground[i];
			avg1Total += tableTotal[j][i];
			double tmpPre = 0;
			if (tableTotal[j][i] != 0) {
				tmpPre = tableCorrect[j][i] / tableTotal[j][i];
			}else {
					if(tableCorrect[j][i] == tableTotal[j][i])
					{
						tmpPre = 1;
					}else {
						tmpPre = 0;
					}
				}
			avg1Precision += tmpPre;
			varTotal += Math.pow(tableTotal[j][i]-avg[j][0],2);
			varPre1 += Math.pow(tmpPre-avg[j][1], 2);
			
			
			double tmpRecall = 0;
			if(ground[i]!=0.0)
			{
				tmpRecall = tableCorrect[j][i]/ground[i];
			}else {
				if(tableCorrect[j][i] == ground[i])
				{
					tmpRecall = 1;
				}else {
					tmpRecall =0;
				}
			}
			varRecall1 += Math.pow(tmpRecall-avg[j][2], 2);
			avg1Recall += tmpRecall;
		}
		avg1Total /= 10;
		avg1Precision /= 10;
		avg1Recall /= 10;
		avgGround /=10;
		
		varTotal /=10;
		varPre1 /= 10;
		varRecall1 /= 10;
		
		
		System.out.println("avgGround:"+avgGround);
		System.out.println("avg1Total:" + avg1Total + ";\tavg1Precision:"
				+ avg1Precision+";\tavg1Recall:"+avg1Recall);
		System.out.println("varTotal:"+varTotal+";\tvarPre1:"+varPre1+";\tvarRecall1:"+varRecall1);
		System.out.println("varTotal:"+Math.sqrt(varTotal)+";\tvarPre1:"+Math.sqrt(varPre1)+";\tvarRecall1:"+Math.sqrt(varRecall1));
		avgGround=0;
		avg1Total = 0;
		avg1Precision = 0;
		avg1Recall=0;
		
		varTotal=0;
		varPre1=0;
		varRecall1=0;
		
		}
	}
}
