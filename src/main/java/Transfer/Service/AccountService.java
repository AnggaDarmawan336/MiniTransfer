package Transfer.Service;

import Transfer.Entity.Account;
import Transfer.Entity.Transaction;
import Transfer.Repository.AccountRepository;
import Transfer.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account createAccount(Account account) {
        return accountRepository.saveAndFlush(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    @Transactional
    public Transaction transferMoney(Long sourceAccountId, Long destinationAccountId, Double amount) throws Exception {
        Optional<Account> sourceAccountOpt = accountRepository.findById(sourceAccountId);
        Optional<Account> destinationAccountOpt = accountRepository.findById(destinationAccountId);

        if (!sourceAccountOpt.isPresent() || !destinationAccountOpt.isPresent()) {
            throw new Exception("Account not found");
        }

        Account sourceAccount = sourceAccountOpt.get();
        Account destinationAccount = destinationAccountOpt.get();

        if (sourceAccount.getBalance() < amount) {
            throw new Exception("Insufficient balance");
        }

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setAmount(amount);
        transaction.setTime(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }
}
