package com.yh.ssc.conf;

import com.yh.ssc.utils.StreamUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-06 09:14
 **/
@Component
@ConfigurationProperties(prefix = "participate")
public class GameProperteries {
    private List<Game> games;
    
    public List<Game> getGames() {
        return games;
    }
    
    public void setGames(List<Game> games) {
        this.games = games;
    }
    
    /**
     * 根据策略倍数，计算实际倍数
     * @param gameId
     * @param strategyMultify
     * @return
     */
    public Integer calMultify(Integer gameId,Integer strategyMultify){
        Integer baseMultify = StreamUtils.ofNullable(games)
                .filter(x->x.getGameId().intValue()==gameId.intValue())
                .findAny()
                .map(Game::getBaseMultify)
                .orElse(1);
        return baseMultify*strategyMultify;
    }
    
    public boolean sendReal(Integer gameId){
        if (gameId==null || gameId<=0){
            return false;
        }
        return StreamUtils.ofNullable(games)
                .filter(x->x.getGameId().intValue()==gameId.intValue())
                .findAny()
                .map(Game::isSendReal)
                .orElse(false);
    }
    
    public boolean allowPlan(Integer gameId){
        if (gameId==null || gameId<=0){
            return false;
        }
        return StreamUtils.ofNullable(games)
                .filter(x->x.getGameId().intValue()==gameId.intValue())
                .findAny()
                .map(Game::getPlan)
                .orElse(false);
    }
    
    @Data
    public static class Game{
        private Integer gameId;
        private Integer row;
        private Boolean plan;
        private Integer baseMultify;
        private boolean sendReal;
    }
}
