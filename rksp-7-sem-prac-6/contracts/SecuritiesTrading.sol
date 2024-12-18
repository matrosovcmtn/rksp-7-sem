// SPDX-License-Identifier: MIT
// IKBO-01-21 Matrosov Danil
pragma solidity ^0.8.0;

contract SecuritiesTrading {
    struct Security {
        string name;
        uint256 price;
        address owner;
        bool isListed;
    }
    
    struct ExchangeRequest {
        uint256 securityOffered;
        uint256 securityRequested;
        address requester;
        address receiver;
        bool isPending;
    }
    
    mapping(uint256 => Security) public securities;
    mapping(uint256 => ExchangeRequest) public exchangeRequests;
    mapping(address => uint256[]) private userSecurities;
    mapping(address => uint256) private balances;
    
    uint256 private nextSecurityId = 1;
    uint256 private nextRequestId = 1;
    
    event SecurityListed(uint256 indexed securityId, string name, uint256 price, address owner);
    event SecurityPurchased(uint256 indexed securityId, address from, address to, uint256 price);
    event ExchangeRequested(uint256 indexed requestId, uint256 securityOffered, uint256 securityRequested, address requester, address receiver);
    event ExchangeCompleted(uint256 indexed requestId, uint256 securityOffered, uint256 securityRequested, address owner1, address owner2);
    event ExchangeRejected(uint256 indexed requestId);
    event BalanceAdded(address indexed user, uint256 amount);
    
    receive() external payable {
        balances[msg.sender] += msg.value;
        emit BalanceAdded(msg.sender, msg.value);
    }
    
    function listSecurity(string memory _name, uint256 _price) public returns (uint256) {
        require(_price > 0, "Price must be greater than 0");
        
        uint256 securityId = nextSecurityId++;
        securities[securityId] = Security({
            name: _name,
            price: _price,
            owner: msg.sender,
            isListed: true
        });
        
        userSecurities[msg.sender].push(securityId);
        emit SecurityListed(securityId, _name, _price, msg.sender);
        return securityId;
    }
    
    function purchaseSecurity(uint256 _securityId) public payable {
        Security storage security = securities[_securityId];
        require(security.isListed, "Security is not listed");
        require(msg.value >= security.price, "Insufficient payment");
        require(security.owner != msg.sender, "Cannot buy your own security");
        
        address previousOwner = security.owner;
        
        _removeFromUserSecurities(previousOwner, _securityId);
        userSecurities[msg.sender].push(_securityId);
        
        security.owner = msg.sender;
        
        balances[previousOwner] += msg.value;
        payable(previousOwner).transfer(msg.value);
        
        emit SecurityPurchased(_securityId, previousOwner, msg.sender, msg.value);
    }
    
    function requestExchange(uint256 _securityOffered, uint256 _securityRequested) public returns (uint256) {
        Security storage securityOffered = securities[_securityOffered];
        Security storage securityRequested = securities[_securityRequested];
        
        require(securityOffered.isListed && securityRequested.isListed, "Both securities must be listed");
        require(securityOffered.owner == msg.sender, "You must own the offered security");
        require(securityRequested.owner != msg.sender, "Cannot exchange with yourself");
        
        uint256 requestId = nextRequestId++;
        exchangeRequests[requestId] = ExchangeRequest({
            securityOffered: _securityOffered,
            securityRequested: _securityRequested,
            requester: msg.sender,
            receiver: securityRequested.owner,
            isPending: true
        });
        
        emit ExchangeRequested(requestId, _securityOffered, _securityRequested, msg.sender, securityRequested.owner);
        return requestId;
    }
    
    function approveExchange(uint256 _requestId) public {
        ExchangeRequest storage request = exchangeRequests[_requestId];
        Security storage securityOffered = securities[request.securityOffered];
        Security storage securityRequested = securities[request.securityRequested];
        
        require(request.isPending, "Exchange request is not pending");
        require(msg.sender == request.receiver, "Only receiver can approve");
        require(securityRequested.owner == msg.sender, "You must own the requested security");
        
        _removeFromUserSecurities(request.requester, request.securityOffered);
        _removeFromUserSecurities(request.receiver, request.securityRequested);
        
        address tempOwner = securityOffered.owner;
        securityOffered.owner = securityRequested.owner;
        securityRequested.owner = tempOwner;
        
        userSecurities[securityOffered.owner].push(request.securityOffered);
        userSecurities[securityRequested.owner].push(request.securityRequested);
        
        request.isPending = false;
        
        emit ExchangeCompleted(_requestId, request.securityOffered, request.securityRequested, 
            securityOffered.owner, securityRequested.owner);
    }
    
    function rejectExchange(uint256 _requestId) public {
        ExchangeRequest storage request = exchangeRequests[_requestId];
        require(request.isPending, "Exchange request is not pending");
        require(msg.sender == request.receiver, "Only receiver can reject");
        
        request.isPending = false;
        emit ExchangeRejected(_requestId);
    }
    
    function getUserSecurities() public view returns (
        uint256[] memory securityIds,
        string[] memory names,
        uint256[] memory prices
    ) {
        uint256[] storage userSecurityIds = userSecurities[msg.sender];
        uint256 length = userSecurityIds.length;
        
        names = new string[](length);
        prices = new uint256[](length);
        securityIds = new uint256[](length);
        
        for (uint256 i = 0; i < length; i++) {
            uint256 securityId = userSecurityIds[i];
            Security storage security = securities[securityId];
            if (security.owner == msg.sender && security.isListed) {
                securityIds[i] = securityId;
                names[i] = security.name;
                prices[i] = security.price;
            }
        }
        
        return (securityIds, names, prices);
    }
    
    function getBalance() public view returns (uint256) {
        return balances[msg.sender];
    }
    
    function _removeFromUserSecurities(address user, uint256 securityId) private {
        uint256[] storage userSecurityIds = userSecurities[user];
        for (uint256 i = 0; i < userSecurityIds.length; i++) {
            if (userSecurityIds[i] == securityId) {
                userSecurityIds[i] = userSecurityIds[userSecurityIds.length - 1];
                userSecurityIds.pop();
                break;
            }
        }
    }
}