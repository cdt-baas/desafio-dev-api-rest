package command_test

import "gihub.com/victorfernandesraton/dev-api-rest/domain"

type CarrierFactoryArgs struct {
	Name string
	CPF  string
}

func CarruerFactory(params []*CarrierFactoryArgs) ([]*domain.Carrier, error) {
	var result []*domain.Carrier

	for _, v := range params {
		data, err := domain.CreateCarrier(v.CPF, v.Name)
		if err != nil {
			return nil, err
		}

		result = append(result, data)
	}

	return result, nil
}

func AccountFacotry(params []*domain.Carrier, agency uint64) ([]*domain.Account, error) {
	var result []*domain.Account

	id := uint64(1)

	for _, v := range params {
		data, err := domain.CreateAccount(*v, agency)
		if err != nil {
			return nil, err
		}
		data.AccountNumber = id
		id += 1
		result = append(result, data)
	}

	return result, nil
}
