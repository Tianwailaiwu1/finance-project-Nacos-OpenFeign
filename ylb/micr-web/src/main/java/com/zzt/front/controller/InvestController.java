package com.zzt.front.controller;

import com.zzt.api.model.User;
import com.zzt.common.constant.RedisKey;
import com.zzt.common.enums.RCode;
import com.zzt.common.util.CommonUtil;
import com.zzt.front.pojo.RespResult;
import com.zzt.front.pojo.invest.RankView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 投资功能类
 */
@CrossOrigin
@Api(tags = "投资理财产品")
@RestController
@RequestMapping("/v1")
public class InvestController extends BaseController {
    /**
     * 投资排行榜
     *
     * @return
     */
    @ApiOperation(value = "投资排行榜", notes = "显示投资金额最高的三位用户信息")
    @GetMapping("/invest/rank")
    public RespResult showInvestRank() {
        //从Redis查询数据
        Set<ZSetOperations.TypedTuple<String>> sets = stringRedisTemplate.boundZSetOps(RedisKey.KEY_INVEST_RANK).reverseRangeWithScores(0, 2);
        //创建RankView集合
        List<RankView> rankViewList = new ArrayList<>();
        //遍历集合
        try {
            sets.forEach(stringTypedTuple -> {
                rankViewList.add(new RankView(CommonUtil.desensitizePhone(stringTypedTuple.getValue()), BigDecimal.valueOf(stringTypedTuple.getScore())));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RespResult result = RespResult.ok();
        result.setList(rankViewList);
        return result;
    }


    /**
     * 购买理财产品，更新排行榜投资信息
     *
     * @param uid
     * @param productId
     * @param money
     * @return
     */
    @ApiOperation(value = "投资理财产品", notes = "购买理财产品，更新投资排行榜信息")
    @PostMapping("/invest/product")
    public RespResult investProduct(@RequestHeader("uid") Integer uid, @RequestParam("productId") Integer productId, @RequestParam("money") BigDecimal money) {
        RespResult result = RespResult.fail();
        //检验参数
        if (uid == null || uid <= 0 || productId == null || productId <= 0 || money == null || money.intValue() % 100 != 0 || money.intValue() < 100) {
            //参数不正确
            result.setCode(RCode.REQUEST_PARAM_ERR);
            return result;
        }
        int investResult = 0;

        try {
            //参数正确
            investResult = investServiceClient.investProduct(uid, productId, money);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        switch (investResult) {
            case 0:
                result.setMsg("投资失败");
                break;
            case 1:
                result = RespResult.ok();
                modifyInvestRank(uid, money);
                break;
            case 2:
                result.setMsg("资金账户不存在");
                break;
            case 3:
                result.setMsg("资金不足");
                break;
            case 4:
                result.setMsg("理财产品不存在");
                break;
            case 5:
                result.setMsg("购买金额不符合条件");
                break;
            case 6:
                result.setMsg("购买的理财产品已满标");
                break;
        }
        return result;
    }

    /**
     * 更新投资排行榜方法
     *
     * @param uid
     * @param money
     */
    private void modifyInvestRank(Integer uid, BigDecimal money) {
        User user = userServiceClient.queryById(uid);
        if (user != null) {
            //更新redis中的投资排行榜信息
            String key = RedisKey.KEY_INVEST_RANK;
            stringRedisTemplate.boundZSetOps(key).incrementScore(user.getPhone(), money.doubleValue());
        }
    }
}
