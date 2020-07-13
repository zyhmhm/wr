package com.wr.feignclient;

import com.wr.feignclient.fb.RepoFeignServiceFB;
import com.wr.pojo.Position;
import com.wr.util.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "repo-service",fallback = RepoFeignServiceFB.class)
public interface RepoFeignService {
    @GetMapping("/repo/getRepoById")
    JsonResult getRepoById(@RequestParam Long repoId);
    @GetMapping("/repo/getRepoByUserId")
    Long doGetOrderMakingToRepo(@RequestParam Long userId);
    @GetMapping("/repo/getPositionById")
    public JsonResult getPosition(@RequestParam Long positionId);
    @PostMapping("/repo/doChangeByEntity")
    JsonResult doChangeByEntity(@RequestBody Position entity);
}
