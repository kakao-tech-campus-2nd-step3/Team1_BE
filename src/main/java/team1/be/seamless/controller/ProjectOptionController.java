package team1.be.seamless.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.be.seamless.dto.OptionDTO;
import team1.be.seamless.dto.OptionDTO.OptionDetail;
import team1.be.seamless.dto.OptionDTO.OptionSimple;
import team1.be.seamless.service.OptionService;
import team1.be.seamless.util.auth.ParsingParam;
import team1.be.seamless.util.page.PageMapper;
import team1.be.seamless.util.page.PageResult;
import team1.be.seamless.util.page.SingleResult;

@Tag(name = "프로젝트 옵션")
@RestController
@RequestMapping("/api/project/option")
public class ProjectOptionController {

    private final OptionService optionService;
    private final ParsingParam parsingParam;

    @Autowired
    public ProjectOptionController(OptionService optionService, ParsingParam parsingParam) {
        this.optionService = optionService;
        this.parsingParam = parsingParam;
    }

    @Operation(summary = "프로젝트 옵션 리스트 조회")
    @GetMapping
    public PageResult<OptionSimple> getOptionList(HttpServletRequest req,
                                                  @Valid OptionDTO.getList param) {
        return PageMapper.toPageResult(
                optionService.getProjectOptionList(param, parsingParam.getRole(req)));
    }

    @Operation(summary = "프로젝트 옵션 조회")
    @GetMapping("/{optionId}")
    public SingleResult<OptionDetail> getOption(HttpServletRequest req,
                                                @Valid @PathVariable("optionId") Long id) {
        return new SingleResult<>(optionService.getOption(id, parsingParam.getRole(req)));
    }

    @Operation(summary = "옵션 생성")
    @PostMapping
    public SingleResult<OptionDetail> createOption(HttpServletRequest req,
                                                   @Valid @RequestBody OptionDTO.OptionCreate create) {
        return new SingleResult<>(optionService.createOption(create, parsingParam.getRole(req)));
    }

    @Operation(summary = "옵션 수정")
    @PutMapping("/{optionId}")
    public SingleResult<OptionDetail> updateOption(HttpServletRequest req,
                                                   @Valid @PathVariable("optionId") Long id,
                                                   @Valid @RequestBody OptionDTO.OptionUpdate update) {
        return new SingleResult<>(
                optionService.updateOption(id, update, parsingParam.getRole(req)));
    }

    @Operation(summary = "옵션 삭제")
    @DeleteMapping("/{optionId}")
    public SingleResult<OptionDetail> deleteOption(HttpServletRequest req,
                                                   @Valid @PathVariable("optionId") Long id) {
        return new SingleResult<>(optionService.deleteOption(id, parsingParam.getRole(req)));
    }

}
