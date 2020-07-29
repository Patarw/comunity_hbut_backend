package VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@ApiModel(value = "分页结果")
@Data
@Builder
public class PageVO<T> {
    @ApiModelProperty("当前页数")
    private int pageNo;       // 当前页数
    @ApiModelProperty("每页数据量")
    private int pageSize;          // 每页数据数量
    @ApiModelProperty("总页数")
    private int totalPage;         // 总页数
    @ApiModelProperty("数据列表")
    private List<T> rows;       // 每行显示内容
}
