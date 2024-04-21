package com.yh.ssc.conf;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-17 19:57
 **/
@Component
public class SumContext {
    
    private List<Integer> targetNumbers;
    
    @PostConstruct
    public void SumContext(){
        targetNumbers = new ArrayList<>();
        for (int start=6;start<=10;start++){
            targetNumbers.add(start);
        }
    }
    
    public List<Integer> getTargetNumbers() {
        return targetNumbers;
    }
    
    public boolean contains(Integer num){
        return targetNumbers.contains(num);
    }
}
