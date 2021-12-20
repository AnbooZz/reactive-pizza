```sh
 Base API Path: api/v1
```
## User API

|Method|Path|Parameter|Description|
|------|------|------|------|
|POST|/user?action=login||Login with username & password|
|POST|/user?action=register||Register user|

|Field|Nullable|Description|
|------|------|------|
|username||username|
|password||password must be same rePassword|
|rePassword||password must be same rePassword|
|email||email|
```json
=> Login API
Request: {
    "username": "string",
    "password": "string"
}
Response: { "success": true }
***************************************************************************************
=> Register API
Request: {
    "email": "string",
    "username": "string",
    "password": "string",
    "rePassword": "string"
}
Response: { "success": true }
```

## Item API
|Method|Path|Parameter|Description|
|------|------|------|------|
|GET|/items||Get all items|
|GET|/items/:id|id: itemId|Get one Item|

|Field|Nullable|Description|
|------|------|------|
|id||Item ID|
|name||Item Name|
|description||Item description|
|ingredients||In description|
|extraText|true|In description|
|sizeInfo||In description |
|size||In SizeInfo|
|cm||In SizeInfo, vd: 24,29|
|price|||
|imgLink|||
|group|||

```json
=> Get items
Resonse: {
    "success": true,
    "result": [{
        "id": "string",
        "name": "string",
        "description": {
            "ingredients": ["string"],
            "extraText": "text",
            "sizeInfo": [{
                 "size": "string",
                "cm": number,
                "price": "string"
            }]
        },
        "imgLink": "string",
        "group": "string",
        "price": "string"
    }]
}
***************************************************************************************
=> Get a item
Response: {
    "id": "string",
    "name": "string",
    "description": {
        "ingredients": ["string"],
        "extraText": "text",
        "sizeInfo": [{
            "size": "string",
            "cm": number,
            "price": number
        }]
    },
    "imgLink": "string",
    "group": "string",
    "price": number
}
```

## Cart API
|Method|Path|Parameter|Description|
|------|------|------|------|
|GET|/cart?userId=:id|id: UserId|Get one cart by userId|
|POST|/cart?userId=:id|id: UserId|Add a item to cart|
|PUT|/cart?userId=:id||Update items in cart|
|DELETE|cart?userId=:userId&itemId=:itemId||Delete item from cart|

|Field|Nullable|Description|
|------|------|------|
|id||CartID|
|totalPrice|||
|coupon|true||
|code||coupon code|
|pickedItems|||
|itemId|||
|imgLink|||
|label|||
|price|||
|quantity|||
|size|||

```json
=> Get cart link with user
Response: {
    "success": true,
    "result": {
        "id": "string",
        "totalPrice": number,
        "coupon": {
            "code": "string"
        },
        "pickedItems": [{
            "itemId": "string",
            "imgLink": "string",
            "label": "string",
            "price": number,
            "quantity": number
        }]
    }
}
***************************************************************************************
=> POST: add item to cart
Request: {
    "itemId": "string",
    "quantity": number,
    "size": "string"
}
Response: { "success": true }
***************************************************************************************
=> PUT: update items in cart
Request: {
    "items": [{
        "itemId": "string",
        "quantity": number
    }],
    "coupon": {
        "code": "string"
    }
}
Response: { "success": true }
***************************************************************************************
=> DELETE: delete item from cart
Response: { "success": true }
```
## Order API
|Method|Path|Parameter|Description|
|------|------|------|------|
|POST|/orders||Create new an order by user|

|Field|Nullable|Description|
|------|------|------|
|userId|||
|cartId|||
|customerInfo|true||
|fullName|||
|phoneNumber|||
|address|||
|memo|true||

```json
=> POST: new order
Request: {
    "userId": "string",
    "cartId": "string",
    "customerInfo": {
        "fullName": "string",
        "phoneNumber": "string",
        "address": "string",
        "memo": "string"
    }
}
Response: { "success": true }
```

## Error Response
```json
{
    "success": false,
    "error": {
        "message": "string"
    }
}
```
