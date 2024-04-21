package com.yh.ssc.profit;

import com.yh.ssc.constants.Common;
import com.yh.ssc.utils.StreamUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-02 19:16
 **/
@Component
@ConfigurationProperties
public class ProfitProperties {
    
    private List<Profit> profits;
    
    public List<Profit> getProfits() {
        return profits;
    }
    
    public void setProfits(List<Profit> profits) {
        Double totalCost = 0d;
        
        for (int i=0;i<profits.size();i++) {
            Profit profit = profits.get(i);
            profit.setRound(profit.getRound());
            profit.setMultify(profit.getMultify());
            profit.setCost(Double.valueOf(profit.getMultify()* Common.SINGLE_AMOUNT));
            totalCost+=profit.getCost();
            
            Double currentProfit = profit.getMultify() * Common.SINGLE_PROFIT;
            profit.setProfit(currentProfit-totalCost);
            profit.setTotalCost(totalCost);
        }
        this.profits = profits;
    }
}
