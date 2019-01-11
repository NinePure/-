package cn.itcast.core.service;

public interface SecKillOrderService {
    void submitOrder(Long seckillId, String name);

    void updateStatus(String out_trade_no, String name, String transaction_id);
}
