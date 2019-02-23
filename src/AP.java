import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class AP {

	public HashMap<String, Integer> solve(ArrayList<String[]> data) {

		Boolean judge = true;

		HashMap<String, Integer> result = new HashMap<String, Integer>();
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < data.get(i).length; j++) {
				if (result.get(data.get(i)[j]) == null) {
					result.put(data.get(i)[j], 1);
				} else if (result.get(data.get(i)[j]) != null) {
					result.put(data.get(i)[j], result.get(data.get(i)[j]) + 1);
				}
			}
		} // 目前支持数为1

		ArrayList<String[]> L1 = new ArrayList<String[]>();
		for (Entry<String, Integer> entry : result.entrySet()) {
			String[] buf = new String[1];
			buf[0] = entry.getKey();
			L1.add(buf);
		}

		// L1存储1项集,首先检查L1是否至少两个不为1,每个节点都是数组
		int buf = 0;
		for (int i = 0; i < L1.size(); i++) {
			if (result.get(L1.get(i)[0]) > 1) {
				buf += 1;
			} else {
				L1.remove(i);
			}
		}
		if (buf < 2)
			judge = false;

		// 创建ArrayList存放L1至Ln,并依次存放
		ArrayList<ArrayList<String[]>> L = new ArrayList<ArrayList<String[]>>();
		L.add(L1);

		for (int count = 2; count < 10000; count++) {
			// int count = 2;
			ArrayList<String[]> Lbuf = new ArrayList<String[]>();// 创建暂时存储的Lcount

			for (int i = 0; i < L.get(count - 2).size(); i++)// 遍历L1的每两个数组
			{
				String[] xbuf = new String[count];// 创建暂时存储候选项的数组

				for (int j = i + 1; j < L.get(count - 2).size(); j++) {
					for (int k = 0; k < L.get(count - 2).get(j).length; k++)// 遍历j数组中的每一个
					{
						for (int m = 0; m < L.get(count - 2).get(i).length; m++)// 遍历i数组中的每一个
						{
							Set<String> set = new HashSet<String>(Arrays.asList(L.get(count - 2).get(i)));
							if (set.contains(L.get(count - 2).get(j)[k]) == false)
							// if (L.get(count - 2).get(i)[m] != L.get(count - 2).get(j)[k])//
							// 如果存在不同的,将i数组+不同的构成新数组
							{
								xbuf = new String[count];
								for (int s = 0; s < L.get(count - 2).get(i).length; s++) {
									xbuf[s] = L.get(count - 2).get(i)[s];
									// System.out.print(xbuf[s]);
								}
								xbuf[count - 1] = L.get(count - 2).get(j)[k];// 将为剪裁结果存入buf数组
								// System.out.println(xbuf[count-1]);
								Lbuf.add(xbuf);
							}
						}
					}
				}
			}

			// 下面进行支持度赋值并在过程中进行剪枝
			for (int i = 0; i < Lbuf.size(); i++)// 遍历候选值
			{
				int counts = 0;// 进行计数
				// for (int k = 0; k < L.get(count-2).size(); k++)
				for (int k = 0; k < data.size(); k++)// 遍历初始数据
				{
					Boolean b = true;
					for (int j = 0; j < Lbuf.get(i).length; j++)// 遍历候选值每一个元素
					// for (int j = 0; j < 2; j++)
					{
						Set<String> set = new HashSet<String>(Arrays.asList(data.get(k)));
						// Set<String> set = new HashSet<String>(L.get(count-2).size());
						// System.out.println(Lbuf.get(i)[j]);
						if (set.contains(Lbuf.get(i)[j]) == false)// 如果不包含
						// if (set.contains("B") == false)// 如果不包含
						{
							b = false;
						}
					}
					if (b)
						counts++;
				}

				// if (counts == 0)// 剪枝
				// {
				// Lbuf.remove(i);
				// }

				String test = "";
				Arrays.sort(Lbuf.get(i));
				// 以下进行剪枝,每个候选值拿出一个字母
				for (int j = 0; j < Lbuf.get(i).length; j++) {
					test += Lbuf.get(i)[j];
				} // 此处是整体字符串
				for (int j = 0; j < Lbuf.get(i).length; j++) {
					Boolean cuttest = false;
					String tick = Lbuf.get(i)[j];
					String tests = test.replace(tick, "");// 随机选择一个去除，组成新字符串
					// System.out.println(tests);
					for (int s = 0; s < L.get(count - 2).size(); s++) {
						String test2 = "";
						for (int t = 0; t < L.get(count - 2).get(s).length; t++) {
							test2 += L.get(count - 2).get(s)[t];
						}
						// System.out.println(test2);
						if (tests.equals(test2)) {
							cuttest = true;
						}
					}

					if (cuttest == false) // 不等于任何一个低一级，因此清除
					{
						// System.out.println(test+"出错");
						for (int m = 0; m < Lbuf.get(i).length; m++) {
							Lbuf.get(i)[m] = "";
						}
						break;
					}
				}

				// System.out.println(counts);// 计数目前正确

				// 下面存入哈希表
				String key = "";
				for (int j = 0; j < Lbuf.get(i).length; j++) {
					key += Lbuf.get(i)[j];
				}
				result.put(key, counts);
			}

			L.add(Lbuf);

			buf = 0;
			ArrayList<String[]> Lbuf2 = new ArrayList<String[]>();
			for (int i = 0; i < L.get(count - 1).size(); i++) {
				String key = "";
				for (int j = 0; j < L.get(count - 1).get(i).length; j++) {
					key += L.get(count - 1).get(i)[j];
				}
				// System.out.println(key);
				if (result.get(key) > 1) {
					buf += 1;
					Lbuf2.add(L.get(count - 1).get(i));
				}
			}
			// System.out.println(buf);

			// for(int i=0;i<Lbuf2.size();i++)
			// {
			// System.out.print(Lbuf2.get(i)[0]);
			// System.out.println(Lbuf2.get(i)[1]);
			// }

			L.remove(count - 1);
			L.add(Lbuf2);
			if (buf < 2) {
				judge = false;
				return result;
			}

		}

		return result;
	}

	public static void main(String[] args) {
		ArrayList<String[]> data = new ArrayList<String[]>();
		String[] exam1 = { "A", "C", "D" };
		String[] exam2 = { "B", "C", "E" };
		String[] exam3 = { "A", "B", "C", "E" };
		String[] exam4 = { "B", "E" };
		data.add(exam1);
		data.add(exam2);
		data.add(exam3);
		data.add(exam4);
		
//		
//		try {
//			File file = new File("C:\\Users\\Abel\\Desktop\\test.txt");
//			InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
//			BufferedReader bf = new BufferedReader(inputReader);
//			// 按行读取字符串
//			String str;
//			while ((str = bf.readLine()) != null) { 
//				String []exam=str.split(" ");
//				data.add(exam);
//			}
//			bf.close();
//			inputReader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		
		System.out.print("请输入你的支持度来进行筛选：");
		Scanner scan = new Scanner(System.in);
		double spd = scan.nextDouble();	
		double support = spd*data.size();
		System.out.print("请输入你的置信度来进行筛选：");
		scan = new Scanner(System.in);
		double cdp=scan.nextDouble();
		
		AP ap = new AP();
		HashMap<String, Integer> result = ap.solve(data);
		
		System.out.println("支持度" + spd*100 + "%"+"并且置信度为"+cdp*100+"%有以下:");
		for (Entry<String, Integer> entry : result.entrySet()) {
			if (entry.getValue() == support)
			{
				String first=(String) entry.getKey().subSequence(0, 1);
				if(entry.getValue()/result.get(first)==cdp)
				{
				System.out.println(entry.getKey());
				}
			}
		}
		
		
		

	}

}
