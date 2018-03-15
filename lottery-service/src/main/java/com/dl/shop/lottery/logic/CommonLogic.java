package com.dl.shop.lottery.logic;

/**
 * 计算注数的基础类
 */
public class CommonLogic {
	/**
	 * 计算注数
	 * 
	 * @param m 下标 --选择结果的个数
	 * @param n 上标 --组合要求的最小值
	 * @return
	 */
	public static int combination(int m, int n) {
		if (m < 0 || n < 0 || m < n) {
			return -1;
		}
		// 数据不符合要求，返回错误信息
		n = n < (m - n) ? n : m - n;// C(m,n)=C(m,m-n)
		if (n == 0) {
			return 1;
		}

		int result = m;// C(m,1);
		for (int i = 2, j = result - 1; i <= n; i++, j--) {
			result = result * j / i;// 得到C(m,i)
		}

		return result;
	}

	/**
	 * 组合排序算法(回溯方法)
	 * 
	 * @param str
	 *            需要排序的字符串
	 * @param n
	 *            拖的个数
	 * @param m
	 *            拖中要选的个数(双色球则是6-胆的个数)
	 * @return
	 */
	public static int combineTotalCount(String str[], int danTotalCount, int n,
			int m) {

		m = m > n ? n : m;

		int ret = 0;
		int[] order = new int[m + 1];
		for (int i = 0; i <= m; i++)
			order[i] = i - 1; // 注意这里order[0]=-1用来作为循环判断标识

		int k = m;
		boolean flag = true; // 标志找到一个有效组合
		while (order[0] == -1) {
			if (flag) // 输出符合要求的组合
			{
				int sumCount = 1;
				for (int j = 1; j <= m; j++) {
					sumCount *= Integer.valueOf(str[order[j]]);
				}
				flag = false;
				ret += sumCount * danTotalCount;
			}
			order[k]++; // 在当前位置选择新的数字
			if (order[k] == n) // 当前位置已无数字可选，回溯
			{
				order[k--] = 0;
				continue;
			}
			if (k < m) // 更新当前位置的下一位置的数字
			{
				order[++k] = order[k - 1];
				continue;
			}
			if (k == m)
				flag = true;
		}

		return ret;
	}

}
