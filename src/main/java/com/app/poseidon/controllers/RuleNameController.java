package com.app.poseidon.controllers;

import com.app.poseidon.domain.RuleName;
import com.app.poseidon.services.RuleNameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@Controller
public class RuleNameController {
    // TODO: Inject RuleName service
    @Autowired
    RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        // TODO: find all RuleName, add to model
        List<RuleName> ruleNameList = ruleNameService.getAllRuleName();
        model.addAttribute("ruleNames", ruleNameList);
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return RuleName list
        if (result.hasErrors()) {
            log.error("Validation errors: {}", result.getAllErrors());
            model.addAttribute("ruleNames", ruleName);
            return "ruleName/add";
        }
        try {
            ruleNameService.save(ruleName);
            return "redirect:/ruleName/list";
        } catch (Exception e) {
            log.error("Save failed", e);
            model.addAttribute("errorMessage", "Save error: " + e.getMessage());
            model.addAttribute("ruleName", ruleName);
            return "ruleName/add";
        }
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get RuleName by Id and to model then show to the form
        RuleName ruleName = ruleNameService.findById(id);
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update RuleName and return RuleName list
        if (result.hasErrors()) {
            model.addAttribute("ruleName", ruleName);
            return "ruleName/update";
        }
        ruleNameService.update(id, ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        // TODO: Find RuleName by Id and delete the RuleName, return to Rule list
        ruleNameService.delete(id);
        return "redirect:/ruleName/list";
    }
}
