import Carton from './Carton';
import useHttp from "../../hooks/use-http";
import {getCartons} from "../../lib/api";
import {useEffect} from "react";
import Form from "react-bootstrap/Form";

const OptionList = props => {
    const {sendRequest, data: cartonList} = useHttp(
        getCartons,
        true
    );

    useEffect(() => {
        if (props.selProduct) {
            sendRequest(props.selProduct);
        }
    }, [sendRequest, props.selProduct]);

    if (cartonList !== null) {
        const optionList = cartonList.map((carton) => (
            <Carton
                key={carton.id}
                id={carton.id}
                noOfUnits={carton.noOfUnits}
                price={carton.price}
                onChange={props.onChange}/>
        ));
        optionList.push(
            <Form.Check key="-1" type="radio">
                <Form.Check.Input id="opt--1" name="option" type="radio" value="-1" onChange={e => props.onChange(e.target.value)}/>
                <Form.Check.Label htmlFor="opt--1">Single Units</Form.Check.Label>
            </Form.Check>
        );
        return optionList;
    } else if (props.selProduct !== null) {
        return <p className="center">No cartons available for the product</p>;
    } else {
        return <p className="center">Please select a product</p>;
    }
};

export default OptionList;