// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <0.9.0;

import "remix_tests.sol";
import "hardhat/console.sol";
import "../contracts/SecuritiesTrading.sol";

contract SecuritiesTradingTest {
    SecuritiesTrading tradingContract;
    
    event TestEvent(string message);
    
    function beforeAll() public {
        tradingContract = new SecuritiesTrading();
    }
    
    // Test 1: Security Listing
    function checkSecurityListing() public {
        console.log("Running checkSecurityListing");
        
        uint256 securityId = tradingContract.listSecurity("Test Security", 1 ether);
        
        (string memory name, uint256 price, address securityOwner, bool isListed) = 
            tradingContract.securities(securityId);
        
        Assert.equal(name, "Test Security", "Security name should match");
        Assert.equal(price, 1 ether, "Security price should match");
        Assert.equal(securityOwner, address(this), "Security owner should be test contract");
        Assert.equal(isListed, true, "Security should be listed");
        
        emit TestEvent("Security listing test passed");
    }
    
    // Test 2: Balance Checking
    function checkBalance() public {
        console.log("Running checkBalance");
        
        uint256 balance = tradingContract.getBalance();
        Assert.equal(balance, 0, "Initial balance should be 0");
        
        emit TestEvent("Balance check test passed");
    }
    
    // Test 3: Get User Securities
    function checkGetUserSecurities() public {
        console.log("Running checkGetUserSecurities");
        
        tradingContract.listSecurity("Security 1", 1 ether);
        tradingContract.listSecurity("Security 2", 2 ether);
        
        (uint256[] memory ids, string[] memory names, uint256[] memory prices) = 
            tradingContract.getUserSecurities();
        
        Assert.equal(ids.length, names.length, "Arrays length should match");
        Assert.equal(names.length, prices.length, "Arrays length should match");
        Assert.notEqual(ids.length, 0, "Should have securities");
        
        emit TestEvent("Get user securities test passed");
    }

}