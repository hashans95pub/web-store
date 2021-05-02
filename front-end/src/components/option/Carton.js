import Form from 'react-bootstrap/Form'

const carton = props => (
    <Form.Check type="radio">
        <Form.Check.Input type="radio" id={`opt-${props.id}`} name="option" value={props.id} onChange={e => props.onChange(e.target.value)}/>
        <Form.Check.Label htmlFor={`opt-${props.id}`}>{props.noOfUnits} Units Carton</Form.Check.Label>
        <Form.Control.Feedback type="valid">{props.price.toFixed(2)}</Form.Control.Feedback>
    </Form.Check>
);

export default carton;