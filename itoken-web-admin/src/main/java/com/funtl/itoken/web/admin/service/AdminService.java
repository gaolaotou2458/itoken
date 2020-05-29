package com.funtl.itoken.web.admin.service;

import com.funtl.itoken.web.admin.service.fallback.AdminServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "itoken-service-admin", fallback = AdminServiceFallBack.class)
public interface AdminService {



}
