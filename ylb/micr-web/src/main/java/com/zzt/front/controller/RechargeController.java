package com.zzt.front.controller;

import com.zzt.api.model.RechargeRecord;
import com.zzt.front.pojo.RespResult;
import com.zzt.front.pojo.recharge.ResultView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Api(tags = "充值功能")
@RestController
@RequestMapping("/v1")
public class RechargeController extends BaseController {

    /**
     * 根据用户id查询相应的充值记录
     *
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询充值记录", notes = "根据用户id查询相应的充值记录分页进行展示")
    @GetMapping("/recharge/records")
    public RespResult queryRechargePage(@RequestHeader("uid") Integer uid, @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo, @RequestParam(value = "pageSize", required = false, defaultValue = "6") Integer pageSize) {
        RespResult result = RespResult.fail();

        if (uid != null && uid > 0) {
            List<RechargeRecord> records = rechargeRecordServiceClient.queryRechargeRecordById(uid, pageNo, pageSize);
            result = RespResult.ok();
            result.setList(toView(records));
        }
        return result;
    }

    /**
     * 将充值记录部分信息进行封装方法
     *
     * @param src
     * @return
     */
    private List<ResultView> toView(List<RechargeRecord> src) {
        //判断是否有充值记录
        if (src == null) {
            return null;
        }
        List<ResultView> target = new ArrayList<>();
        src.forEach(rechargeRecord -> {
            target.add(new ResultView(rechargeRecord));
        });
        return target;
    }

}
