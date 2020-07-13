package com.wr.service;

import com.wr.pojo.Repo;
import com.wr.util.JsonResult;

public interface RepoService {
    JsonResult getRepoInfo();


    JsonResult doUpdateRepo(Repo entity);

    JsonResult saveRepo(Repo entity);

    JsonResult getAllRepo();
}
