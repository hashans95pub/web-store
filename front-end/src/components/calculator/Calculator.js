import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button'

import classes from './Calculator.module.css';
import OptionList from '../option/OptionList';
import {useEffect, useState} from "react";
import useHttp from "../../hooks/use-http";
import {getCartonsPrice, getUnitsPrice} from "../../lib/api";

const Calculator = props => {
    const [optionId, setOptionId] = useState(null);
    const [quantity, setQuantity] = useState(0);
    const [price, setPrice] = useState(0);

    let {sendRequest: sendRequestToGetgetUnitsPrice, status: getUnitsPriceStatus, data: unitsPrice} = useHttp(getUnitsPrice, true);
    let {sendRequest: sendRequestToGetCartonsPrice, status: getCartonsPriceStatus, data: cartonsPrice} = useHttp(getCartonsPrice, true);

    useEffect(() => {
        if (parseInt(optionId) === -1 && getUnitsPriceStatus === 'completed') {
            setPrice(unitsPrice);
        } else if (optionId > 0 && getCartonsPriceStatus === 'completed') {
            setPrice(cartonsPrice);
        }
    }, [getUnitsPriceStatus, getCartonsPriceStatus, unitsPrice, cartonsPrice]);

    const getPrice = () => {
        if (props.productId > 0 && quantity > 0) {
            if (optionId > 0) {
                sendRequestToGetCartonsPrice({
                    cartonId: optionId,
                    noOfCartons: quantity
                });
            } else if (parseInt(optionId) === -1) {
                sendRequestToGetgetUnitsPrice({
                    productId: props.productId,
                    noOfUnits: quantity
                });
            }
        }
    };

    return (
        <div className={classes.container}>
            <OptionList selProduct={props.productId} onChange={setOptionId}/>
            {props.productId != null && optionId !== null ?
                <Form.Control className={classes.qty} size="lg" type="number" placeholder="Enter Quantity" min="0"
                              max="1000" value={quantity} onChange={e => setQuantity(e.target.value)}/>
                : props.productId != null && optionId === null ? <p className="center">Please select an option</p> : ''
            }
            {quantity > 0 ? <center><Button className={classes['calc-btn']} variant="primary" onClick={getPrice}>Calculate
                Price</Button>
            </center> : ''}

            {price === 0 || <p className={classes.price}>{price.toFixed(2)}</p>}
        </div>
    );
};

export default Calculator;