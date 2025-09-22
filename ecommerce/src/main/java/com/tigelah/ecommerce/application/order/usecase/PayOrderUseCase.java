package com.tigelah.ecommerce.application.order.usecase;

import com.tigelah.ecommerce.application.order.command.PayOrderCommand;
import com.tigelah.ecommerce.domains.order.Order;


public interface PayOrderUseCase { Order handle(PayOrderCommand userId); }
