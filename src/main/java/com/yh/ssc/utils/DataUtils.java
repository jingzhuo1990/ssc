package com.yh.ssc.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-18 20:25
 **/
public class DataUtils {
    public static boolean isSequentiallyIncremented(List<String> list) {
        // 列表为空或只有一个元素时，默认认为符合条件
        if (list.size() <= 1) {
            return false;
        }
        
        // 从第二个元素开始遍历，因为我们需要和前一个元素比较
        for (int i = 1; i < list.size(); i++) {
            // 如果当前元素不是前一个元素加1，就返回false
            String before = String.valueOf(Long.valueOf(list.get(i - 1)) - 1);
            String now = list.get(i);
            if (!StringUtils.equals(before,now)) {
                return false;
            }
        }
        
        // 所有元素都符合条件，返回true
        return true;
    }
    
}
